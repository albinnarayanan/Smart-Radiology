package com.smartradiology.project.controller;

import com.smartradiology.project.security.jwt.JwtUtils;
import com.smartradiology.project.security.request.LoginRequest;
import com.smartradiology.project.security.response.AuthenticationResult;
import com.smartradiology.project.security.response.MessageResponse;
import com.smartradiology.project.security.response.RefreshResponse;
import com.smartradiology.project.security.response.UserInfoResponse;
import com.smartradiology.project.security.services.UserDetailsImpl;
import com.smartradiology.project.service.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Value("${spring.app.jwtRefreshExpirationMs}")
    private Long refreshExpirationMs;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("{spring.profiles.active}")
    private String activeProfile;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            AuthenticationResult result = authService.login(loginRequest);
            System.out.println(result.getResponse());

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE,
                            result.getAccessTokenCookie().toString(),
                            result.getRefreshTokenCookie().toString())
                    .body(result.getResponse());
        }catch (Exception e){
            ResponseCookie cleanRefresh = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(!activeProfile.equals("dev"))
                    .path("/api")
                    .maxAge(0)   // ← delete cookie
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, cleanRefresh.toString())
                    .body(new MessageResponse("Invalid username or password"));

        }
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest  request){
        try{
            String oldRefreshToken = jwtUtils.getRefreshTokenFromCookies(request);
            if (oldRefreshToken == null) {
                throw new SecurityException("No refresh token provided");
            }
            RefreshResponse refreshResult = authService.refreshAccessToken(oldRefreshToken);
            ResponseCookie newAccessCookie = jwtUtils.generateJwtCookie(refreshResult.getAccessToken());
            ResponseCookie newRefreshCookie = ResponseCookie.from("refreshToken", refreshResult.getRefreshToken())
                    .httpOnly(true)
                    .secure(!activeProfile.equals("dev"))

                    .path("/api")
                    .maxAge(Duration.ofMillis(refreshExpirationMs))
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, newAccessCookie.toString()
                            ,newRefreshCookie.toString())
                    .body(new MessageResponse("Token Refreshed"));
        }
        catch (Exception e) {
            // Clear refresh cookie on ANY failure
            ResponseCookie cleanRefresh = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(!activeProfile.equals("dev"))
                    .path("/api")
                    .maxAge(0)
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, cleanRefresh.toString())
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // More robust check
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Safety check in case someone changed the principal type
        if (!(auth.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid user principal type");
        }

        // Build the response (never return full UserDetailsImpl directly!)
        UserInfoResponse response = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getUsername(),
                userDetails.getEntityType(),
                userDetails.getEmail()
                // add more fields if needed: roles, avatarUrl, lastLogin, etc.
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){

        String accessToken = jwtUtils.getJwtFromCookies(request);
        String refreshToken = jwtUtils.getRefreshTokenFromCookies(request);
        if(refreshToken==null){
            logger.debug("Logout called without refresh token - partial logout only");

        }
        ResponseCookie cleanCookie = authService.logoutUser(accessToken, refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanCookie.toString())
                .body(new MessageResponse("You have been signed out"));
    }


}
