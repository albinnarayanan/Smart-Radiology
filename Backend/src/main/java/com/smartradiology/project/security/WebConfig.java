package com.smartradiology.project.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${frontend.url}")
    private String frontEndURL;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("CORS allowed origin: "+frontEndURL);

        registry.addMapping("/**")
                .allowedOrigins(frontEndURL)
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
