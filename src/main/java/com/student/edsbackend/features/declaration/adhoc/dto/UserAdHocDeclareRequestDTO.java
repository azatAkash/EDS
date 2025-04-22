package com.student.edsbackend.features.declaration.adhoc.dto;

import com.student.edsbackend.features.enums.UserDeclarationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for creating a new UserAdHocDeclare
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdHocDeclareRequestDTO {
    @NotNull(message = "User ID is required")
    private Integer userId;
    private Boolean hasAgreedWithStatements;
}