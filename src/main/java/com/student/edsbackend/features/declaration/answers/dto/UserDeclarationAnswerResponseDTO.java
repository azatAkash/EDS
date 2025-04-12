package com.student.edsbackend.features.declaration.answers.dto;

import com.student.edsbackend.features.enums.UserDeclarationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for returning information about saved user declaration answers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDeclarationAnswerResponseDTO {
    private Integer userDeclarationId;
    private Integer userId;
    private String userName;
    private Integer declarationId;
    private LocalDateTime creationDate;
    private UserDeclarationStatus status;
    private Integer additionalAnswersCount;
    private String message;
}