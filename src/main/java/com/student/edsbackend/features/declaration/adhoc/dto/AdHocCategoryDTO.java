package com.student.edsbackend.features.declaration.adhoc.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for AdHocCategory
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdHocCategoryDTO {
    
    @Schema(description = "Unique identifier of the ad-hoc category")
    private Integer id;
    
    @Schema(description = "Multilingual description of the category with language codes as keys (en, ru, kz)")
    private Map<String, String> description;
}