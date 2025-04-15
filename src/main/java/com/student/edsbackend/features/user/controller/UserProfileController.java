package com.student.edsbackend.features.user.controller;

import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserProfileController {

    private final UserRepository userRepository;

    /**
     * Endpoint to get the current authenticated user's profile
     * @return UserDTO containing the current user's information
     */
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile() {
        // Get current user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Find the user by email
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Current user not found"));
        
        // Convert to DTO and return
        UserDTO userDTO = UserDTO.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .firstname(currentUser.getFirstname())
                .lastname(currentUser.getLastname())
                .middlename(currentUser.getMiddlename())
                .role(currentUser.getRole())
                .position(currentUser.getPosition())
                .department(currentUser.getDepartment())
                .isActive(currentUser.getIsActive())
                .isDeleted(currentUser.getIsDeleted())
                .registrationDate(currentUser.getRegistrationDate())
                .build();

        return ResponseEntity.ok(userDTO);
    }
}