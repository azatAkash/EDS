package com.student.edsbackend.features.declaration.initial.dal.questions;

import java.util.List;
import java.util.Map;

import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOptionDTO;
import com.student.edsbackend.features.enums.QuestionType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link InitialDeclarationQuestion}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitialDeclarationQuestionDTO {
    private Integer id;
    private Short orderNumber;
    private Integer declarationId;
    private Map<String, String> description;
    private QuestionType questionType;
    private Map<String, String> note;
    private Boolean isRequired;
    private Boolean isDeleted;
}