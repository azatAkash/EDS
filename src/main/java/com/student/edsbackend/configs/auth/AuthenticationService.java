package com.student.edsbackend.configs.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.edsbackend.configs.JwtService;
import com.student.edsbackend.features.token.Token;
import com.student.edsbackend.features.token.TokenRepository;
import com.student.edsbackend.features.token.TokenType;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserRegistrationRequestDTO;
import com.student.edsbackend.features.user.dal.UserRepository;
import com.student.edsbackend.features.user.service.UserValidationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserValidationService validationService;

    /**
     * Registers a new user based on the provided UserRegistrationRequestDTO.
     * Validates the user data using the UserValidationService.
     *
     * @param registrationDTO the user registration data transfer object
     * @return an AuthenticationResponse containing the access and refresh tokens
     */
    public AuthenticationResponse register(UserRegistrationRequestDTO registrationDTO) {
        // Convert UserRegistrationRequestDTO to UserDTO for validation
        UserDTO userDTO = UserDTO.builder()
                .email(registrationDTO.getEmail())
                .password(registrationDTO.getPassword())
                .firstname(registrationDTO.getFirstname())
                .lastname(registrationDTO.getLastname())
                .middlename(registrationDTO.getMiddlename())
                .role(registrationDTO.getRole())
                .position(registrationDTO.getPosition())
                .department(registrationDTO.getDepartment())
                .build();
                
        // Validate the incoming UserDTO
        List<String> errors = validationService.validateRegistrationRequest(userDTO);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }
        // Add duplicate email check here
        if (repository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        // Map UserRegistrationRequestDTO to the User entity
        User user = User.builder()
                .firstname(registrationDTO.getFirstname())
                .lastname(registrationDTO.getLastname())
                .middlename(registrationDTO.getMiddlename())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .role(registrationDTO.getRole())
                .position(registrationDTO.getPosition())
                .department(registrationDTO.getDepartment())
                .isActive(true)
                .registrationDate(java.time.LocalDateTime.now())
                .build();

        User savedUser = repository.save(user);
        
        // Generate JWT access and refresh tokens
        String accessToken = jwtService.generateToken(Collections.singletonMap("role", user.getRole()), user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Only persist the refresh token in the database
        saveRefreshToken(savedUser, refreshToken);

        UserDTO responseUserDTO = UserDTO.builder()
            .id(savedUser.getId())
            .email(savedUser.getEmail())
            .firstname(savedUser.getFirstname())
            .lastname(savedUser.getLastname())
            .middlename(savedUser.getMiddlename())
            .role(savedUser.getRole())
            .position(savedUser.getPosition())
            .department(savedUser.getDepartment())
            .isActive(savedUser.getIsActive())
            .registrationDate(savedUser.getRegistrationDate())
            .build();

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(responseUserDTO)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        
        if (user.getIsActive() == false) {
            System.out.println("Account is not active");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Account is not active");    
        }
        // Generate tokens with role information
        String accessToken = jwtService.generateToken(Collections.singletonMap("role", user.getRole()), user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Only store the refresh token in the database
        saveRefreshToken(user, refreshToken);

        // Map the User entity to a UserDTO (be cautious about sensitive fields like
        // password)
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .middlename(user.getMiddlename())
                .role(user.getRole())
                .position(user.getPosition())
                .department(user.getDepartment())
                .isActive(user.getIsActive())
                .registrationDate(user.getRegistrationDate())
                .build();

        
        // Build and return the response with tokens and user data
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userDTO)
                .build();

    }

    /**
     * Saves a refresh token for a user in the database.
     * Access tokens are not stored in the database.
     * Refresh tokens have a 7-day expiration and can only be used once.
     * When a refresh token is used, it is deleted from the database and a new one is issued.
     * 
     * @param user the user to save the token for
     * @param refreshToken the refresh token to save
     */
    private void saveRefreshToken(User user, String refreshToken) {
        Token token = Token.builder()
                .user(user)
                .token(refreshToken)
                .build();
        tokenRepository.save(token);
    }

    /**
     * Removes all refresh tokens for a user from the database.
     * This is typically called during logout or when all user sessions need to be invalidated.
     * 
     * @param user the user whose tokens should be removed
     */
    private void revokeAllUserTokens(User user) {
        var userTokens = tokenRepository.findAllTokensByUser(user.getId());
        if (userTokens.isEmpty()) {
            return;
        }
        // Instead of marking tokens as expired/revoked, we delete them
        tokenRepository.deleteAll(userTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        
        // Check if the authorization header is valid
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid authorization header");
            return;
        }
        
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        
        if (userEmail == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid token");
            return;
        }
        
        try {
            // Check if the token exists in the database
            var storedToken = tokenRepository.findByToken(refreshToken)
                    .orElse(null);
            
            if (storedToken == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Refresh token not found");
                return;
            }
            
            User user = repository.findByEmail(userEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            
            // Verify JWT expiration using JwtService instead of database flags
            if (!jwtService.isTokenValid(refreshToken, user)) {
                // If token is invalid or expired, remove it from database
                tokenRepository.delete(storedToken);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired refresh token");
                return;
            }
            
            // Generate new access token and refresh token
            String accessToken = jwtService.generateToken(Collections.singletonMap("role", user.getRole()), user);
            String newRefreshToken = jwtService.generateRefreshToken(user);
            
            // Remove the used refresh token and save the new one
            tokenRepository.delete(storedToken);
            saveRefreshToken(user, newRefreshToken);
            
            // Note: We don't store access tokens in the database anymore
            
            // Create user DTO for response
            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .middlename(user.getMiddlename())
                    .role(user.getRole())
                    .position(user.getPosition())
                    .department(user.getDepartment())
                    .isActive(user.getIsActive())
                    .registrationDate(user.getRegistrationDate())
                    .build();
            
            // Build complete response with user information
            AuthenticationResponse authResponse = AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(newRefreshToken)
                    .user(userDTO)
                    .build();
            
            // Set response headers and write response
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error refreshing token: " + e.getMessage());
        }
    }
    
    /**
     * Refreshes the access token using a refresh token provided in the request body.
     * This method is designed to be used with a JSON request containing a refresh_token field.
     * The refresh token is verified for validity and existence in the database.
     * After successful renewal, the used refresh token is revoked to ensure one-time use.
     *
     * @param refreshTokenRequest the request containing the refresh token
     * @return an AuthenticationResponse containing the new access token, new refresh token, and user information
     */
    /**
     * Refreshes the access token using a refresh token provided in the request body.
     * This method is designed to be used with a JSON request containing a refresh_token field.
     * The refresh token is verified for validity and existence in the database.
     * After successful verification, the used refresh token is removed from the database and a new one is created.
     *
     * @param refreshTokenRequest the request containing the refresh token
     * @return an AuthenticationResponse containing the new access token, new refresh token, and user information
     */
    public AuthenticationResponse refreshTokenFromBody(RefreshTokenRequest refreshTokenRequest) {
        final String refreshToken = refreshTokenRequest.getRefreshToken();
        final String userEmail = jwtService.extractUsername(refreshToken);
        
        if (userEmail == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        }
        
        // Check if the token exists in the database
        var storedToken = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token not found"));
        
        User user = repository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        // Verify JWT expiration using JwtService instead of database flags
        if (!jwtService.isTokenValid(refreshToken, user)) {
            // If token is invalid or expired, remove it from database
            tokenRepository.delete(storedToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token");
        }
        
        // Generate new access token and refresh token
        String accessToken = jwtService.generateToken(Collections.singletonMap("role", user.getRole()), user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        
        // Remove the used refresh token and save the new one
        tokenRepository.delete(storedToken);
        saveRefreshToken(user, newRefreshToken);
        
        // Note: We don't store access tokens in the database anymore
        
        // Create user DTO for response
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .middlename(user.getMiddlename())
                .role(user.getRole())
                .position(user.getPosition())
                .department(user.getDepartment())
                .isActive(user.getIsActive())
                .registrationDate(user.getRegistrationDate())
                .build();
        
        // Build and return the response with the new refresh token
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .user(userDTO)
                .build();
    }

    /**
     * Handles user logout by verifying the access token and deleting the refresh token from the database.
     * The access token is extracted from the Authorization header in Bearer format.
     * The refresh token is provided in the request body as JSON {refresh_token}.
     *
     * @param request the HTTP request containing the Authorization header with the access token
     * @param response the HTTP response to set status codes
     * @param logoutRequest the request body containing the refresh token to be deleted
     */
    public void logout(HttpServletRequest request, HttpServletResponse response, LogoutRequest logoutRequest) {
        // Verify the access token in the Authorization header
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        // Extract and validate the access token
        final String accessToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(accessToken);
        
        if (userEmail == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        // Get the refresh token from the request body
        final String refreshToken = logoutRequest.getRefreshToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        // Find and delete the refresh token from the database
        var storedToken = tokenRepository.findByToken(refreshToken).orElse(null);
        if (storedToken != null) {
            // Delete the token from the database
            tokenRepository.delete(storedToken);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

