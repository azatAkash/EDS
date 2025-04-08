package com.student.edsbackend.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.student.edsbackend.features.user.dal.Permission;
import com.student.edsbackend.features.user.dal.Role;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutService logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints that don't require authentication
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html")
                        .permitAll()

                        // User management endpoints - restricted by role
                        .requestMatchers("/api/v1/users/**").hasAnyRole(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers(GET, "/api/v1/users/**")
                        .hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers(POST, "/api/v1/users/**")
                        .hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers(PUT, "/api/v1/users/**")
                        .hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers(DELETE, "/api/v1/users/**")
                        .hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name())

                        // Management endpoints - restricted by role
                        .requestMatchers("/api/v1/management/**")
                        .hasAnyRole(Role.MANAGER.name(), Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers(GET, "/api/v1/management/**")
                        .hasAnyAuthority(Role.MANAGER.name(), Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers(POST, "/api/v1/management/**")
                        .hasAnyAuthority(Role.MANAGER.name(), Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers(PUT, "/api/v1/management/**")
                        .hasAnyAuthority(Role.MANAGER.name(), Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers(DELETE, "/api/v1/management/**")
                        .hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name())

                        // All other endpoints require authentication
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(
                                (request, response, authentication) -> SecurityContextHolder.clearContext()));

        return http.build();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Removes the default "ROLE_" prefix
    }

}