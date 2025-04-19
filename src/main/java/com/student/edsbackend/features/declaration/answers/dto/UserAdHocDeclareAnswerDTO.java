package com.student.edsbackend.features.declaration.answers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for transferring UserAdHocDeclareAnswer data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdHocDeclareAnswerDTO {
    private Integer id;
    private Integer userAdHocDeclareId;
    private Integer categoryId;
    private Map<String, String> categoryDescription; // Multilingual description from AdHocCategory
    private String otherCategory;
    private String conflictDescription;
}

/**
 * DTO for creating or updating UserAdHocDeclareAnswer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UserAdHocDeclareAnswerRequestDTO {
    private Integer userAdHocDeclareId;
    private Integer categoryId;
    private String otherCategory;
    private String conflictDescription;
}

/**
 * DTO for returning a list of UserAdHocDeclareAnswers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UserAdHocDeclareAnswerResponseDTO {
    private Integer userAdHocDeclareId;
    private String message;
    private Boolean success;
}