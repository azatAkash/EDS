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

import com.student.edsbackend.features.user.dal.Permission;
import com.student.edsbackend.features.user.dal.Role;
import com.student.edsbackend.features.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import static org.springframework.http.HttpMethod.*;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

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

                        .requestMatchers("/api/v1/declarations/**")
                        .hasAnyRole(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                        .requestMatchers(GET,"/api/v1/declarations/**")
                        .hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(POST,"/api/v1/declarations/**")
                        .hasAuthority(Role.SUPER_ADMIN.name())
                        .requestMatchers(PUT,"/api/v1/declarations/**")
                        .hasAuthority(Role.SUPER_ADMIN.name())
                        .requestMatchers(DELETE,"/api/v1/declarations/**")
                        .hasAuthority(Role.SUPER_ADMIN.name())

                        // All other endpoints require authentication
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            
                            // Create the same response as in GlobalExceptionHandler
                            ApiResponse apiResponse = new ApiResponse("You don't have permission to use this endpoint");
                            
                            // Log the access denied event for security monitoring
                            System.out.println("Access denied: " + accessDeniedException.getMessage());
                            
                            // Convert ApiResponse to JSON and write to response
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.writeValue(response.getWriter(), apiResponse);
                        }));

        return http.build();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Removes the default "ROLE_" prefix
    }

}