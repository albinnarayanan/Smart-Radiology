package com.smartradiology.project.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JwtUtils {

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private Long jwtExpirationMs;

    @Value("${spring.app.jwtCookieName}")
    private String jwtCookie;

    @Value("${spring.app.jwtRefreshExpirationMs}")
    private Long refreshExpirationMs;

    @Value("{spring.profiles.active}")
    private String activeProfile;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null && cookie.getValue() != null && !cookie.getValue().isBlank()) {
            logger.debug("Found JWT cookie: {}", jwtCookie);
            return cookie.getValue();
        }
        return null;
    }

    // Fallback: Authorization header
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Generate a single JWT cookie
    public ResponseCookie generateJwtCookie(String token) {
        return ResponseCookie.from(jwtCookie, token)
                .httpOnly(true)
                .secure(!activeProfile.equals("dev"))      // true in production with HTTPS
                .path("/api")

                .maxAge(Duration.ofMillis(jwtExpirationMs))
                .build();
    }

    // Clear the cookie
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, "")
                .httpOnly(true)
                .secure(!activeProfile.equals("dev"))
                .path("/api")
                .maxAge(0)
                .build();
    }

    // Generate JWT with claims
    public String generateAccessToken(String username, String entityType) {
        String authority = "ENTITY_" + entityType.toUpperCase();
        String jti = UUID.randomUUID().toString();
        return Jwts.builder()
                .id(jti)
                .subject(username)
                .claim("entityType", entityType)
                .claim("authorities", List.of(authority))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    public String generateRefreshToken(String username){
        return Jwts.builder()
                .subject(username)
                .claim("type","refresh")
                .expiration(new Date(System.currentTimeMillis()+refreshExpirationMs))
                .signWith(key())
                .compact();
    }
    public String jtiFromJwtToken(String token){
        try{
            return Jwts.parser().verifyWith((SecretKey) key()).build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getId();
        }
        catch(Exception e){
            return null;
        }
    }
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public String getUserNameFromRefreshToken(String token){
        try {
            return Jwts.parser().verifyWith((SecretKey) key()).build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public String getUserNameFromJWTToken(String token) {
        return Jwts.parser().verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public String getEntityTypeFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("entityType", String.class);
    }

    public Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }








}
