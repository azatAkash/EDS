package com.student.edsbackend.features.declaration.initial.dal.option;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link AdditionalAnswerOption} creation and update requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalAnswerOptionRequestDTO {

    @Schema(description = "Option ID this additional answer option belongs to", example = "1")
    private Integer optionId;

    @Schema(description = "Additional answer option description", example = "Please provide details")
    private Map<String, String> description;


    @Schema(description = "Whether this additional answer is required", example = "true")
    private Boolean isRequired;
}
