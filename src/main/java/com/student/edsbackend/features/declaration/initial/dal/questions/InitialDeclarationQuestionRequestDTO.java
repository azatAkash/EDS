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
 * DTO for {@link InitialDeclarationQuestion} creation requests that excludes ID field
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitialDeclarationQuestionRequestDTO {
    @Schema(description = "Question order number", example = "1")
    private Short orderNumber;
    
    @Schema(description = "Declaration ID this question belongs to", example = "1")
    private Integer declarationId;
    
    @Schema(
    description = "Question description in multiple languages",
    example = "{\"en\": \"Do you have any conflicts of interest?\", \"ru\": \"Есть ли у вас конфликт интересов?\", \"kz\": \"Сізде мүдделер қақтығысы бар ма?\"}")
    private Map<String, String> description;
    
    @Schema(description = "Type of question", example = "YES_NO")
    private QuestionType questionType;
    
    @Schema(
    description = "Question description in multiple languages",
    example = "{\"en\": \"Do you have any conflicts of interest?\", \"ru\": \"Есть ли у вас конфликт интересов?\", \"kz\": \"Сізде мүдделер қақтығысы бар ма?\"}")
    private Map<String, String> note;
    
    @Schema(description = "Whether the question is required", example = "true")
    private Boolean isRequired;
}