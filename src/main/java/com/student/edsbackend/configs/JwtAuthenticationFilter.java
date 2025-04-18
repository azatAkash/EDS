package com.student.edsbackend.configs;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = authHeader.substring(7);
            String userEmail;

            try {
                userEmail = jwtService.extractUsername(jwt);
                System.out.println("Extracted username: " + userEmail);

                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                    // Extract role from token
                    String tokenRole = jwtService.extractRole(jwt);
                    
                    // Get current user role from database
                    String currentRole = userDetails.getAuthorities().stream()
                            .findFirst()
                            .map(GrantedAuthority::getAuthority)
                            .orElse(null);
                    
                    // Debug logging
                    System.out.println("Token role: " + tokenRole);
                    System.out.println("Current DB role: " + currentRole);
                    System.out.println("Token validity: " + jwtService.isTokenValid(jwt, userDetails));

                    // Validate token and check if roles match
                    if (jwtService.isTokenValid(jwt, userDetails) && tokenRole != null && tokenRole.equals(currentRole)) {
                        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(currentRole));
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                                null, authorities);
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else if (!tokenRole.equals(currentRole)) {
                        // Role mismatch - token role doesn't match current user role
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.getWriter().write("Role mismatch: Your permissions have changed since this token was issued");
                        response.setContentType("application/json");
                        return; // Stop filter chain for role mismatch
                    }
                }
            } catch (ExpiredJwtException ex) {
                // Handle expired JWT token specifically
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("JWT token has expired");
                response.setContentType("application/json");
                return; // Stop filter chain for expired tokens
            } catch (JwtException ex) {
                // Handle other JWT exceptions
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid JWT token");
                response.setContentType("application/json");
                return; // Stop filter chain for invalid tokens
            }

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            // Catch any other exceptions to prevent 500 errors
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Authentication error: " + ex.getMessage());
            response.setContentType("application/json");
        }
    }
}
