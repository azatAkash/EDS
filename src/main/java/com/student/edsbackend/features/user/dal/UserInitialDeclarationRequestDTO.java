package com.student.edsbackend.features.user.dal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating a new UserInitialDeclaration The active declaration will be
 * automatically fetched by the service
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInitialDeclarationRequestDTO {

    @NotNull(message = "User ID is required")
    private Integer userId;
}
