package com.student.edsbackend.web.service.implementations;

import com.student.edsbackend.configs.ApiResponse;
import com.student.edsbackend.features.user.dal.Role;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserRepository;
import com.student.edsbackend.features.user.service.UserValidationService;
import com.student.edsbackend.web.service.UserService;
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
    public Optional<User> findUser(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(UserDTO dto) {
        User user = User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .middlename(dto.getMiddlename())
                .role(dto.getRole())

                .build();
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UserDTO dto) {
        // Assuming that UserDTO contains an id field to identify the user
        Optional<User> optionalUser = userRepository.findById(dto.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());
            user.setFirstname(dto.getFirstname());
            user.setLastname(dto.getLastname());
            user.setMiddlename(dto.getMiddlename());
            user.setRole(dto.getRole());
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id: " + dto.getId());
        }
    }
    
    @Override
    public String editUser(UserDTO dto, Integer userId) {
        // Find the user to edit
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
        
        // Update user fields
        if (dto.getEmail() != null && !user.getEmail().equals(dto.getEmail())) {
            if (dto.getEmail().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
            } else if (!UserValidationService.isValidEmail(dto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format");
            }else if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
            }

            user.setEmail(dto.getEmail());
        }
        if (dto.getRole()== Role.SUPER_ADMIN){
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
        System.out.println("Hello I am here");
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
        userRepository.deleteById(id);
    }
}
