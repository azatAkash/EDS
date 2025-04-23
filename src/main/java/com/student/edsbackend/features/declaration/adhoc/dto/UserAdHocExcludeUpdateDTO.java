package com.student.edsbackend.features.declaration.adhoc.dto;

import com.student.edsbackend.features.enums.UserDeclarationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Request DTO for creating or updating UserAdHocExclude
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdHocExcludeUpdateDTO {
    private Boolean isConfirmed;
    private UserDeclarationStatus status;
}