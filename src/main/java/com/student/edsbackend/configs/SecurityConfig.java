package com.student.edsbackend.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.student.edsbackend.features.user.dal.Permission;

import static com.student.edsbackend.features.user.dal.Permission.*;
import static com.student.edsbackend.features.user.dal.Role.ADMIN;
import static com.student.edsbackend.features.user.dal.Role.MANAGER;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req ->
                req
                    // Management endpoints require specific roles
                    .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                    .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(Permission.MANAGER.getPermission())
                    .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(Permission.MANAGER.getPermission())
                    .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(Permission.MANAGER.getPermission())
                    .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(Permission.MANAGER.getPermission())
                    
                    // Public endpoints for auth and declarations only
                    .requestMatchers("/api/v1/auth/**", "/api/v1/declarations/**").permitAll()
                    
                    // User endpoints restricted to ADMIN and SUPER_ADMIN only
                    .requestMatchers("/api/v1/users/**").hasAnyRole(ADMIN.name(), "SUPER_ADMIN")
                    
                    // All other requests require authentication
                    .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout ->
                logout.logoutUrl("/api/v1/auth/logout")
                      .addLogoutHandler(logoutHandler)
                      .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
            );

        return http.build();
    }


}
