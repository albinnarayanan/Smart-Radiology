package com.smartradiology.project.service;

import com.smartradiology.project.security.jwt.JwtUtils;
import com.smartradiology.project.security.request.LoginRequest;
import com.smartradiology.project.security.response.AuthenticationResult;
import com.smartradiology.project.security.response.RefreshResponse;
import com.smartradiology.project.security.response.UserInfoResponse;
import com.smartradiology.project.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AuthServiceImpl implements  AuthService{

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${spring.app.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${spring.app.jwtRefreshExpirationMs}")
    private long refreshExpirationMs;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("{spring.profiles.active}")
    private String activeProfile;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final String BLACKLIST_KEY_PREFIX = "jwt:blacklist:";
    private static final String REFRESH_TOKEN_PREFIX = "jwt:refresh:";
    private static final Duration ACCESS_TOKEN_BLACKLIST_TTL = Duration.ofMinutes(60);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public AuthenticationResult login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        //Generate Token
        String accessToken = jwtUtils.generateAccessToken(userDetails.getUsername(),userDetails.getEntityType());
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(accessToken);

        //Generate Refresh Token
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername());
        //Store Refresh Token with proper TTL
        storeRefreshToken(userDetails.getUsername(),refreshToken);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)           // ← prevents JS access
                .secure(!activeProfile.equals("dev"))
                .path("/api")
                .maxAge(Duration.ofMillis(refreshExpirationMs))
                .build();

        UserInfoResponse userInfoResponse = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getUsername(),
                userDetails.getEntityType(),
                userDetails.getEmail());

        return new AuthenticationResult(userInfoResponse,jwtCookie, refreshCookie);
    }



    @Override
    public ResponseCookie logoutUser(String accessToken, String refreshToken) {
        String jti = jwtUtils.jtiFromJwtToken(accessToken);
        if(jti !=null){
            String key = BLACKLIST_KEY_PREFIX  + jti;
            redisTemplate.opsForValue().set(key,"blacklisted",Duration.ofMillis(jwtExpirationMs));

        }
        //invalidate Refresh Token
        String username = jwtUtils.getUserNameFromRefreshToken(refreshToken);
        if(username!=null){
            String refreshKey = REFRESH_TOKEN_PREFIX + username;
            redisTemplate.delete(refreshKey);
        }

        return jwtUtils.getCleanJwtCookie();
    }

    private void storeRefreshToken(String username, String refreshToken){
        String key = REFRESH_TOKEN_PREFIX + username;
        redisTemplate.opsForValue().set(key,refreshToken,Duration.ofMillis(refreshExpirationMs));
    }

    public boolean isTokenBlackListed(String token){
        String jti = jwtUtils.jtiFromJwtToken(token);
        if(jti == null){
            return false;
        }
        return redisTemplate.hasKey(BLACKLIST_KEY_PREFIX + jti);
    }
    public RefreshResponse refreshAccessToken(String refreshToken){
        String username = jwtUtils.getUserNameFromRefreshToken(refreshToken);
        if(username==null){
            throw new IllegalArgumentException("Invalid Refresh Token");
        }
        String storedRefreshToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + username);
        if(!refreshToken.equals(storedRefreshToken)){
            // Token reuse attempt or stolen → revoke
            redisTemplate.delete(REFRESH_TOKEN_PREFIX + username);
            throw new SecurityException("Refresh token invalid or reused");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        String newRefreshToken = jwtUtils.generateRefreshToken(username);
        String newAccessToken = jwtUtils.generateAccessToken(username,userDetails.getEntityType());
        storeRefreshToken(username, newRefreshToken);
        return new RefreshResponse(newAccessToken,newRefreshToken);
    }
}
