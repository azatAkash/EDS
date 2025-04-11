package com.student.edsbackend.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(1) // Важно: выше по приоритету
public class SwaggerSecurityConfig {

    @Bean
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**",
                             "/v2/api-docs/**", "/webjars/**", "/configuration/**")
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable()) // 🔥 это правильный способ
            );

        return http.build();
    }
}

