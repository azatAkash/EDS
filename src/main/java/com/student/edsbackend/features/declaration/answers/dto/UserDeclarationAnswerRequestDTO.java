package com.student.edsbackend.features.declaration.answers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for receiving user declaration answers from client
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDeclarationAnswerRequestDTO {
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
    }
}