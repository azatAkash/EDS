package com.student.edsbackend.configs.auth;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.mail.NotificationService;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserRegistrationRequestDTO;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service; 
    private final NotificationService notificationService;

    @GetMapping("/testEmail")
    public ResponseEntity<ApiResponse> test(@RequestParam String text) {
        try {
            notificationService.sendTest(text);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                   .body(new ApiResponse("Error sending email: " + e.getMessage()));
        }
        return ResponseEntity.ok(new ApiResponse(text));
    }

    @GetMapping("/test1")
    public ResponseEntity<ApiResponse> test1() {
        return ResponseEntity.ok(new ApiResponse("Hello"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequestDTO registrationDTO) {
            AuthenticationResponse authResponse = service.register(registrationDTO);
            return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
            AuthenticationResponse authResponse = service.authenticate(request);
            return ResponseEntity.ok(authResponse);

    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshTokenFromBody(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            // Call the service to refresh the token using the request body
            AuthenticationResponse response = service.refreshTokenFromBody(refreshTokenRequest);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            // Return the appropriate status code and error message
            return ResponseEntity.status(e.getStatusCode())
                    .body(new ApiResponse(e.getReason()));
        } catch (Exception e) {
            // If an unexpected exception occurs, return an internal server error
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error refreshing token: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, @RequestBody LogoutRequest logoutRequest) {
        // Extract the token from the request and validate the refresh token
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid authorization header"));
        }
        
        // Call the service to handle logout
        service.logout(request, response, logoutRequest);
        
        // Check the response status and return appropriate response
        int status = response.getStatus();
        if (status == HttpServletResponse.SC_OK) {
            return ResponseEntity.ok(new ApiResponse("Logged out successfully"));
        } else if (status == HttpServletResponse.SC_BAD_REQUEST) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid token format or missing refresh token"));
        } else if (status == HttpServletResponse.SC_NOT_FOUND) {
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(new ApiResponse("Refresh token not found"));
        } else {
            return ResponseEntity.status(status).body(new ApiResponse("Logout failed"));
        }
    }
    
    /**
     * Endpoint for changing a user's password.
     * Requires authentication. The current password must be provided for verification.
     * 
     * @param passwordChangeRequest contains the current password and new password
     * @return success message or error details
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDTO passwordChangeRequest) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();
            
            // Call service to change password
            service.changePassword(currentUserEmail, passwordChangeRequest);
            
            return ResponseEntity.ok(new ApiResponse("Password changed successfully"));
        } catch (ResponseStatusException e) {
            // Return the appropriate status code and error message
            return ResponseEntity.status(e.getStatusCode())
                    .body(new ApiResponse(e.getReason()));
        } catch (Exception e) {
            // If an unexpected exception occurs, return an internal server error
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error changing password: " + e.getMessage()));
        }
    }
}
