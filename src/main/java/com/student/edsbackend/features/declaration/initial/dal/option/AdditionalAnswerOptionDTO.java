package com.student.edsbackend.features.declaration.initial.dal.option;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link AdditionalAnswerOption}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalAnswerOptionDTO {
    private Integer id;
    private Integer optionId;
    private String description;
    private Boolean isRequired;
    private Boolean isDeleted;
}