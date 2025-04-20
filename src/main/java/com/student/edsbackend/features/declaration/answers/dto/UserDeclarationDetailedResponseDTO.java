package com.student.edsbackend.features.declaration.answers.dto;

import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    private Integer declarationId;
    private String declarationName;
    private Boolean hasConflict;
    private LocalDateTime creationDate;
    private UserDeclarationStatus status;
    private UserDTO createdBy;
    private UserDTO user;
    private UserDTO responsible;
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
        private Short orderNumber;
        private Map<String, String> description;
        private String questionType;
        private Map<String, String> note;
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
        private Map<String, String> description;
        private Map<String, String> additionalAnswerDescription;
        private Boolean multipleAdditionalAnswers;
        private Boolean isConflict;
        
        // User's answer data
        private Boolean isAnswered;
        private String answer;
        private Boolean hasConflict;
        private AdditionalAnswersContainerDTO additionalAnswers;
    }
    
    /**
     * DTO representing a container for additional answers with questions and answers
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdditionalAnswersContainerDTO {
        private List<AdditionalQuestionDTO> questions;
        private List<AdditionalAnswersGroupDTO> answers;
    }
    
    /**
     * DTO representing an additional question
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdditionalQuestionDTO {
        private Integer id;
        private Map<String, String> description;
        private Boolean isRequired;
    }
    
    /**
     * DTO representing a group of additional answers with an order index
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdditionalAnswersGroupDTO {
        private Short orderIndex;
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
        private Integer id;
        private Integer additionalAnswerId;
        private String answer;
    }
}