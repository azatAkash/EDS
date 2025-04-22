package com.student.edsbackend.features.declaration.adhoc.dto;

import java.util.List;

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
    private Boolean hasAgreedWithStatements;
    List<UserAdHocDeclareAnswerRequestDTO> answers;
}



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UserAdHocDeclareAnswerRequestDTO {
    private Integer categoryId;
    private String otherCategory;
    private String conflictDescription;
}