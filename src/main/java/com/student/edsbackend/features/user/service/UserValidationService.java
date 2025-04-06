package com.student.edsbackend.features.user.service;

import com.student.edsbackend.features.user.dal.Role;
import com.student.edsbackend.features.user.dal.UserDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service for validating user data before registration or updates.
 * Provides methods to validate email format, password strength, and other user fields.
 */
@Service
public class UserValidationService {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@nu\\.edu\\.kz$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final int MIN_PASSWORD_LENGTH = 8;

    /**
     * Validates a registration request using UserDTO.
     *
     * @param userDTO The user data transfer object to validate
     * @return List of validation error messages (empty if validation passes)
     */
    public List<String> validateRegistrationRequest(UserDTO userDTO) {
        List<String> errors = new ArrayList<>();

        // Validate email
        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            errors.add("Email is required");
        } else if (!isValidEmail(userDTO.getEmail())) {
            errors.add("Invalid email format");
        }
         // Add duplicate email check here
        
    
        // Validate password
        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            errors.add("Password is required");
        } else if (!isValidPassword(userDTO.getPassword())) {
            errors.add("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
        }

        // Validate first name
        if (userDTO.getFirstname() == null || userDTO.getFirstname().trim().isEmpty()) {
            errors.add("First name is required");
        }

        // Validate last name
        if (userDTO.getLastname() == null || userDTO.getLastname().trim().isEmpty()) {
            errors.add("Last name is required");
        }

        
        if (userDTO.getRole() == null || userDTO.getRole() != Role.USER) {
            errors.add("Invalid role");
        }

        return errors;
    }

    /**
     * Validates email format.
     *
     * @param email The email to validate
     * @return true if email is valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validates password strength.
     *
     * @param password The password to validate
     * @return true if password is valid, false otherwise
     */
    public boolean isValidPassword(String password) {
        return password != null && password.length() >= MIN_PASSWORD_LENGTH;
    }
}
