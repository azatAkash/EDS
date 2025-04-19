package com.student.edsbackend.features.declaration.answers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for creating or updating multiple UserAdHocDeclareAnswers in a single request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdHocDeclareAnswerRequestDTO {
    private Integer userAdHocDeclareId;
    private List<AnswerDTO> answers;
    
    /**
     * DTO representing a single answer within a batch request
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDTO {
        private Integer id; // Optional, used for updates
        private Integer categoryId;
        private String otherCategory;
        private String conflictDescription;
    }
}

/**
 * DTO for response after batch processing UserAdHocDeclareAnswers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UserAdHocDeclareAnswerBatchResponseDTO {
    private Integer userAdHocDeclareId;
    private String message;
    private Boolean success;
    private List<Integer> createdAnswerIds;
    private List<Integer> updatedAnswerIds;
}