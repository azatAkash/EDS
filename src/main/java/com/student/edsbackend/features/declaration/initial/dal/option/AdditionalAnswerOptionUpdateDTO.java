package com.student.edsbackend.features.declaration.initial.dal.option;

import java.util.Map;

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

    @Schema(
        description = "Question description in multiple languages",
        example = "{\"en\": \"Do you have any conflicts of interest?\", \"ru\": \"Есть ли у вас конфликт интересов?\", \"kz\": \"Сізде мүдделер қақтығысы бар ма?\"}"
    )    private Map<String, String> description;


    @Schema(description = "Whether this additional answer is required", example = "true")
    private Boolean isRequired;
}
