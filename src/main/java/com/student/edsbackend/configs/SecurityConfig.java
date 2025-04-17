package com.student.edsbackend.configs;

import com.student.edsbackend.features.user.dal.Role;
import com.student.edsbackend.features.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@Order(2) // ⚠️ После Swagger-конфига
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // 🔒 эта цепочка только для API
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(OPTIONS, "/**").permitAll() // Allow preflight requests
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/user/**").hasAnyRole(Role.ADMIN.name(), Role.SUPER_ADMIN.name(), Role.MANAGER.name(), Role.USER.name())
                .requestMatchers("/api/v1/users/**").hasAnyRole(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                .requestMatchers(GET, "/api/v1/users/**").hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                .requestMatchers(POST, "/api/v1/users/**").hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                .requestMatchers(PUT, "/api/v1/users/**").hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                .requestMatchers(DELETE, "/api/v1/users/**").hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                
                .requestMatchers("/api/v1/management-plans/**").hasAnyRole(Role.MANAGER.name(), Role.ADMIN.name(), Role.SUPER_ADMIN.name(), Role.USER.name())
                .requestMatchers(GET, "/api/v1/management-plans/**").hasAnyAuthority(Role.MANAGER.name(), Role.ADMIN.name(), Role.SUPER_ADMIN.name(), Role.USER.name())
                .requestMatchers(POST, "/api/v1/management-plans/**").hasAnyAuthority(Role.MANAGER.name(), Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                .requestMatchers(PUT, "/api/v1/management-plans/**").hasAnyAuthority(Role.MANAGER.name(), Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                .requestMatchers(DELETE, "/api/v1/management-plans/**").hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                
                .requestMatchers("/api/v1/initial-declarations/**").hasAnyRole(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                .requestMatchers(GET, "/api/v1/initial-declarations/**").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                .requestMatchers(POST, "/api/v1/initial-declarations/**").hasAuthority(Role.SUPER_ADMIN.name())
                .requestMatchers(PUT, "/api/v1/initial-declarations/**").hasAuthority(Role.SUPER_ADMIN.name())
                .requestMatchers(DELETE, "/api/v1/initial-declarations/**").hasAuthority(Role.SUPER_ADMIN.name())
                
                .requestMatchers(POST, "/api/v1/initial-declarations/answers/**").hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name(), Role.MANAGER.name(), Role.USER.name())
                .requestMatchers(GET, "/api/v1/initial-declarations/answers/**").hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name(), Role.MANAGER.name(), Role.USER.name())
                .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                .accessDeniedHandler((request, response, ex) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    ApiResponse apiResponse = new ApiResponse("You don't have permission to use this endpoint");
                    new ObjectMapper().writeValue(response.getWriter(), apiResponse);
                })
                );

        return http.build();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // убираем префикс ROLE_
    }
}
