package com.spam.mail.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // tüm endpoint'ler
                .allowedOrigins("http://localhost:5173") // React frontend URL canlıya alırken .allowedOrigins("http://localhost:5173", "https://yourapp.com")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // Eğer cookie/token gönderiyorsan true olmalı
    }
}
