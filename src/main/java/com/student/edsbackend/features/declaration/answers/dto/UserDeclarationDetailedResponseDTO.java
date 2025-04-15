package com.student.edsbackend.features.declaration.answers.dto;

import com.student.edsbackend.features.enums.UserDeclarationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Comprehensive DTO that combines declaration questions with user answers
 * for returning detailed information about user declaration
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDeclarationDetailedResponseDTO {
    private Integer userDeclarationId;
    private Integer userId;
    private String userName;
    private Integer declarationId;
    private String declarationName;
    private LocalDateTime creationDate;
    private UserDeclarationStatus status;
    private String message;
    private List<QuestionWithAnswerDTO> questionsWithAnswers;
    
    /**
     * DTO representing a question with its options and user answers
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionWithAnswerDTO {
        private Integer id;
        private Integer orderNumber;
        private String description;
        private String questionType;
        private String note;
        private Boolean isRequired;
        private List<OptionWithAnswerDTO> optionsWithAnswers;
    }
    
    /**
     * DTO representing an option with user's answer
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionWithAnswerDTO {
        private Integer id;
        private String description;
        private String additionalAnswerDescription;
        private Boolean multipleAdditionalAnswers;
        private Boolean isConflict;
        
        // User's answer data
        private Boolean isAnswered;
        private String answer;
        private List<AdditionalAnswerGroupDTO> additionalAnswers;
    }
    
    /**
     * DTO representing a group of additional answers with an order index
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdditionalAnswerGroupDTO {
        private List<AdditionalAnswerDTO> answers;
    }
    
    /**
     * DTO representing a single additional answer
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdditionalAnswerDTO {
        private Integer additionalAnswerId;
        private String description;
        private Boolean isRequired;
        private String answer;
    }
}