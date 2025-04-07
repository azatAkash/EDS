package com.student.edsbackend.features.user.service.implementations;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.user.dal.Role;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserRepository;
import com.student.edsbackend.features.user.service.UserService;
import com.student.edsbackend.features.user.service.UserValidationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<UserDTO> findUser(Integer id) {
        return userRepository.findById(id)
                .filter(user -> !user.getIsDeleted())
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .middlename(user.getMiddlename())
                        .role(user.getRole())
                        .position(user.getPosition())
                        .department(user.getDepartment())
                        .isActive(user.getIsActive())
                        .isDeleted(user.getIsDeleted())
                        .registrationDate(user.getRegistrationDate())
                        .build());
    }


    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> !user.getIsDeleted())
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .middlename(user.getMiddlename())
                        .role(user.getRole())
                        .position(user.getPosition())
                        .department(user.getDepartment())
                        .isActive(user.getIsActive())
                        .isDeleted(user.getIsDeleted())
                        .registrationDate(user.getRegistrationDate())
                        .build())
                .toList();
    }

    @Override
    public String editUser(UserDTO dto, Integer userId) {
        // Find the user to edit
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
        if (dto.getPassword() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot change password using this endpoint");
        }
        // Update user fields
        if (dto.getEmail() != null && !user.getEmail().equals(dto.getEmail())) {
            if (dto.getEmail().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
            } else if (!UserValidationService.isValidEmail(dto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format");
            } else if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
            }

            user.setEmail(dto.getEmail());
        }
        if (dto.getRole() == Role.SUPER_ADMIN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only one SUPER_ADMIN can be created");
        }
        // Only SUPER_ADMIN can change roles
        if (dto.getRole() != null && user.getRole() != dto.getRole()) {
            user.setRole(dto.getRole());
        }

        if (dto.getFirstname() != null) {
            user.setFirstname(dto.getFirstname());
        }

        if (dto.getLastname() != null) {
            user.setLastname(dto.getLastname());
        }

        if (dto.getMiddlename() != null) {
            user.setMiddlename(dto.getMiddlename());
        }
        
        if (dto.getPosition() != null) {
            user.setPosition(dto.getPosition());
        }

        if (dto.getDepartment() != null) {
            user.setDepartment(dto.getDepartment());
        }

        if (dto.getIsActive() != null) {
            user.setIsActive(dto.getIsActive());
        }

        // Save the updated user
        userRepository.save(user);

        return "User was successfully updated";
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
        user.setIsDeleted(true);
        userRepository.save(user);
    }
}
