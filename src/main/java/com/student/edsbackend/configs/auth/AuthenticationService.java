package com.student.edsbackend.configs.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.edsbackend.configs.JwtService;
import com.student.edsbackend.features.token.Token;
import com.student.edsbackend.features.token.TokenRepository;
import com.student.edsbackend.features.token.TokenType;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
     * Registers a new user based on the provided UserDTO.
     * Validates the user data using the UserValidationService.
     *
     * @param userDTO the user data transfer object to register
     * @return an AuthenticationResponse containing the access and refresh tokens
     */
    public AuthenticationResponse register(UserDTO userDTO) {
        // Validate the incoming UserDTO
        List<String> errors = validationService.validateRegistrationRequest(userDTO);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", errors));
        }
        // Add duplicate email check here
        if (repository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        // Map UserDTO to the User entity
        User user = User.builder()
                .firstname(userDTO.getFirstname())
                .lastname(userDTO.getLastname())
                .middlename(userDTO.getMiddlename())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(userDTO.getRole())
                .position(userDTO.getPosition())
                .department(userDTO.getDepartment())
                .isActive(true)
                .build();

        User savedUser = repository.save(user);
        
        // Generate JWT access and refresh tokens
        String jwtToken = jwtService.generateToken(Collections.singletonMap("role", user.getRole()), user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Persist the JWT token in the database
        saveUserToken(savedUser, jwtToken);

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
            .build();

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
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
        String jwtToken = jwtService.generateToken(Collections.singletonMap("role", user.getRole()), user);
        String refreshToken = jwtService.generateRefreshToken(user);

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
                .build();

        
        // Build and return the response with tokens and user data
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(userDTO)
                .build();

    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(Collections.singletonMap("role", user.getRole()), user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
