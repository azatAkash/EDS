package com.student.edsbackend.features.declaration.initial.dal.option;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link InitialDeclarationOption} creation and update requests that excludes ID field
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitialDeclarationOptionRequestDTO {
    @Schema(description = "Question ID this option belongs to", example = "1")
    private Integer questionId;
    
    @Schema(description = "Option description", example = "Yes")
    private Map<String, String> description;
    
    @Schema(description = "Description for additional answer if required", example = "Please provide details")
    private String additionalAnswerDescription;
    
    @Schema(description = "Whether multiple additional answers are allowed", example = "false")
    private Boolean multipleAdditionalAnswers;
    
    @Schema(description = "Whether this option represents a conflict of interest", example = "true")
    private Boolean isConflict;
}