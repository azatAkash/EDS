package com.student.edsbackend.features.declaration.initial.dal.option;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link AdditionalAnswerOption} update requests Does not include
 * optionId as it cannot be changed during updates
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalAnswerOptionUpdateDTO {

    @Schema(description = "Additional answer option description", example = "Please provide details")
    private String description;

    @Schema(description = "Whether this additional answer is required", example = "true")
    private Boolean isRequired;
}
