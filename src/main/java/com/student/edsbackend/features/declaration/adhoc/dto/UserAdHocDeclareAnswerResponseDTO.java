package com.student.edsbackend.features.declaration.adhoc.dto;

import java.util.List;
import java.util.Map;

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
public class UserAdHocDeclareAnswerResponseDTO {
    private Integer categoryId;
    private Map<String, String> categoryDescription;
    private String otherCategory;
    private String conflictDescription;

}