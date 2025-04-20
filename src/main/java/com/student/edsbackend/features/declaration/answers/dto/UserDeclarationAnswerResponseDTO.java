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

/**
 * DTO for returning information about saved user declaration answers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDeclarationAnswerResponseDTO {
    private Integer userDeclarationId;
    private UserDTO user;
    private UserDTO createdBy;
    private UserDTO responsible;
    private Integer declarationId;
    private LocalDateTime creationDate;
    private UserDeclarationStatus status;
    private String message;
    private List<AnswerDTO> answers;

    /**
     * DTO representing a single answer to a declaration option
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDTO {
        private Integer optionId; // initial_declaration_options.id
        private Boolean isAnswered; // user_declaration_answers.is_answered
        private String answer; // user_declaration_answers.answer
        private Boolean hasConflict; // user_declaration_answers.has_conflict
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
        private Integer additionalAnswerId; // additional_answer_options.id -> user_declaration_additional_answers.answer_option_id
        private String answer; // user_declaration_additional_answers.answer
    };
}