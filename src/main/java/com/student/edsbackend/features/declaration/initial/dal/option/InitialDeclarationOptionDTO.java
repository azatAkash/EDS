package com.student.edsbackend.features.declaration.initial.dal.option;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link InitialDeclarationOption}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitialDeclarationOptionDTO {
    private Integer id;
    private Integer questionId;
    private Map<String, String> description;
    private Map<String, String> additionalAnswerDescription;
    private Boolean multipleAdditionalAnswers;
    private Boolean isConflict;
    private Boolean isDeleted;
}