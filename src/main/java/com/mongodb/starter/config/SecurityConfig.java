package com.mongodb.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection for all requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ws/**").permitAll() // Allow all access to WebSocket endpoint
                        .anyRequest().permitAll() // Other requests need authentication
                );

        return http.build();
    }
}
