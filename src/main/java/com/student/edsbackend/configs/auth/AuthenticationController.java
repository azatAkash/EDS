package com.student.edsbackend.configs.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.student.edsbackend.configs.ApiResponse;
import com.student.edsbackend.features.user.dal.UserDTO;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @GetMapping("/test")
    public ResponseEntity<ApiResponse> test(@RequestParam String text) {
        return ResponseEntity.ok(new ApiResponse(text + "123"));
    }

    @GetMapping("/test1")
    public ResponseEntity<ApiResponse> test1() {
        return ResponseEntity.ok(new ApiResponse("Hello"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        try {
            // Process registration and return the authentication tokens in JSON format
            AuthenticationResponse authResponse = service.register(userDTO);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            // Return a 400 Bad Request with a JSON body containing the error message
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage()));
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            // Process authentication and return the authentication tokens in JSON format
            AuthenticationResponse authResponse = service.authenticate(request);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            // Return a 400 Bad Request with a JSON body containing the error message
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage()));
        }
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // The refreshToken method writes directly to the HttpServletResponse output stream in JSON format
        service.refreshToken(request, response);
    }
}
