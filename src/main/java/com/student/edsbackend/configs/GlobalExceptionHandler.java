package com.student.edsbackend.configs;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.student.edsbackend.features.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUsernameNotFound(UsernameNotFoundException ex) {
        ApiResponse response = new ApiResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse> handleResponseStatusException(ResponseStatusException ex) {
        ApiResponse response = new ApiResponse(ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        // Extract more information from the exception message if available
        String message = ex.getMessage();
        String responseMessage = "Access denied: You do not have sufficient permissions to access this resource";
        
        // If there's specific information about required role in the exception message, include it
        if (message != null && !message.isEmpty()) {
            if (message.contains("ROLE_")) {
                // Extract the role information from the exception message
                responseMessage += ". Required role: " + message.substring(message.indexOf("ROLE_"));
            } else {
                responseMessage += ". Details: " + message;
            }
        }
        
        // Log the access denied event for security monitoring
        // This helps administrators track unauthorized access attempts
        System.out.println("Access denied: " + message);
        
        ApiResponse response = new ApiResponse(responseMessage);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    
    /**
     * Handles BadCredentialsException which occurs when login credentials are incorrect
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ApiResponse response = new ApiResponse("Authentication failed: Invalid username or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    /**
     * Handles DisabledException which occurs when a disabled user attempts to authenticate
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse> handleDisabledException(DisabledException ex) {
        ApiResponse response = new ApiResponse("Authentication failed: Account is disabled");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    
    /**
     * Handles InsufficientAuthenticationException which occurs when authentication is required but not provided
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ApiResponse> handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
        ApiResponse response = new ApiResponse("Authentication required: Please log in to access this resource");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    /**
     * Handles any other authentication exceptions not covered by specific handlers
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex) {
        ApiResponse response = new ApiResponse("Authentication failed: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
