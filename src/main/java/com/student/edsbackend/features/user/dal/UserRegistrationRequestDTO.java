package com.student.edsbackend.features.user.dal;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * DTO for user registration requests that excludes ID field
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequestDTO {
    @Schema(description = "User's email address", example = "user@example.com")
    private String email;
    
    @Schema(description = "User's password", example = "password123")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    @Schema(description = "User's last name", example = "Smith")
    private String lastname;
    
    @Schema(description = "User's first name", example = "John")
    private String firstname;
    
    @Schema(description = "User's middle name", example = "Robert")
    private String middlename;
    
    @Schema(description = "User's role", example = "USER")
    private Role role;
    
    @Schema(description = "User's position", example = "Software Developer")
    private String position;
    
    @Schema(description = "User's department", example = "Engineering")
    private String department;
}