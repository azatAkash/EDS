package com.student.edsbackend.configs.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.student.edsbackend.features.ApiResponse;
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
            AuthenticationResponse authResponse = service.register(userDTO);
            return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
            AuthenticationResponse authResponse = service.authenticate(request);
            return ResponseEntity.ok(authResponse);

    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // The refreshToken method writes directly to the HttpServletResponse output stream in JSON format
        service.refreshToken(request, response);
    }
}
