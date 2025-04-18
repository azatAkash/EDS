package com.student.edsbackend.features.user.dal;

import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestionDTO;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Detailed DTO for transferring UserInitialDeclaration data with nested questions, options, and additional options
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitialDeclarationDetailedDTO {

    private Integer id;
    private String name;
    private LocalDateTime creationDate;
    private LocalDateTime activationDate;
    private Boolean isActive;
    private Boolean isDeleted;
    private UserDTO createdBy;
    private List<QuestionDTO> questions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDTO {
        private Integer id;
        private Integer orderNumber;
        private Integer declarationId;
        private Map<String, String> description;
        private String questionType;
        private Map<String, String> note;
        private Boolean isRequired;
        private Boolean isDeleted;
        private List<OptionDTO> options;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionDTO {
        private Integer id;
        private Integer questionId;
        private Map<String, String> description;
        private String additionalAnswerDescription;
        private Boolean multipleAdditionalAnswers;
        private Boolean isConflict;
        private Boolean isDeleted;
        private List<AdditionalOptionDTO> additionalOption;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdditionalOptionDTO {
        private Integer id;
        private Integer optionId;
        private Map<String, String> description;
        private Boolean isRequired;
        private Boolean isDeleted;
    }
}