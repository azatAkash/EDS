package com.student.edsbackend.features.declaration.initial.dal.questions;

import java.util.List;

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
    
    @Schema(description = "Question description", example = "Do you have any conflicts of interest?")
    private String description;
    
    @Schema(description = "Type of question", example = "YES_NO")
    private QuestionType questionType;
    
    @Schema(description = "Additional note for the question", example = "Please select all that apply")
    private String note;
    
    @Schema(description = "Whether the question is required", example = "true")
    private Boolean isRequired;
}