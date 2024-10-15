package com.mongodb.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Apply CORS to all API endpoints
//                .allowedOrigins("http://127.0.0.1:3000", "http://192.168.1.102:3000") // Thêm nhiều URL
                .allowedOrigins("*") // Cho phép tất cả các nguồn gốc
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
        registry.addMapping("/ws/**")
                .allowedOrigins("*")
                .allowCredentials(true);
    }
}

