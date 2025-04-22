package com.student.edsbackend.features.management.dto;

import com.student.edsbackend.features.enums.ManagementPlanStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for creating a new UserManagementPlan
 * Either userDeclarationId or adHocId must be provided
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserManagementPlanRequestDTO {
    private Integer userDeclarationId;
    private Integer adHocDeclareId;
    
    private Boolean actionRequired;
    private Integer actionId;
    private String actionDetails;
    private LocalDateTime executionDate;
}