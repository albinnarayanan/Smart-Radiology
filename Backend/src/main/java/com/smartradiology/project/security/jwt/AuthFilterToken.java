package com.smartradiology.project.security.jwt;

import com.smartradiology.project.security.services.UserDetailsServiceImpl;
import com.smartradiology.project.service.AuthServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilterToken extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthServiceImpl authService;


    private static final Logger logger = LoggerFactory.getLogger(AuthFilterToken.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws
                                    ServletException, IOException {

        String requestURI = request.getRequestURI();
        logger.debug("AuthTokenFilter caller for URI: {}",request.getRequestURI());
        try{
            String jwt = parseJwt(request);
            if(jwt==null){
                filterChain.doFilter(request,response);
                return;
            }
            if(authService.isTokenBlackListed(jwt)){
                logger.warn("Rejected blacklisted token for URI: {}",requestURI);
                sendUnauthorized(response,"Token has been revoked");
                return;
            }
            if(!jwtUtils.validateToken(jwt)){
                logger.debug("Invalid or expired JWT token");
                filterChain.doFilter(request,response);
                return;
            }
            String username = jwtUtils.getUserNameFromJWTToken(jwt);
            String entityType = jwtUtils.getEntityTypeFromJwtToken(jwt);

            UserDetails userDetails = userDetailsService.loadUserByUsernameAndEntityType(username,entityType);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            logger.debug("Successfully authenticated {} : {}",entityType,username);



        }
    catch (ExpiredJwtException e) {
        logger.debug("Expired JWT token: {}", e.getMessage());
    } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
        logger.warn("Invalid JWT token: {}", e.getMessage());
    } catch (Exception e) {
        logger.error("Authentication error for URI {}: {}", requestURI, e.getMessage(), e);
    }

        filterChain.doFilter(request, response);


    }

    private String parseJwt(HttpServletRequest request) {
        // Try cookie first

        String jwtFromCookie = jwtUtils.getJwtFromCookies(request);
        if (jwtFromCookie != null) {
            return jwtFromCookie;
        }

        // Fallback to Authorization header
        return jwtUtils.getJwtFromHeader(request);
    }
    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException{
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" +message +"\"}");
    }
}
