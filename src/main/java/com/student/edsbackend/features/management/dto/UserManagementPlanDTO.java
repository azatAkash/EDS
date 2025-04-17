package com.student.edsbackend.features.management.dto;

import com.student.edsbackend.features.enums.ManagementPlanStatus;
import com.student.edsbackend.features.user.dal.UserDTO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for transferring UserManagementPlan data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserManagementPlanDTO {
    private Integer id;
    private Integer userDeclarationId;
    private Integer adHocId;
    private LocalDateTime creationDate;
    private UserDTO createdBy;
    private ManagementPlanStatus status;
    private Boolean isDeleted;
    private Boolean isAmended;
    private Boolean actionRequired;
    private Integer actionId;
    private String actionDetails;
    private LocalDateTime executionDate;
    private LocalDateTime notificationDate;
    private LocalDateTime confirmationDate;
    private String reasonNonExecution;
    private Boolean acknowledgedByUser;
    private Boolean ensuredByManager;
    private String userDisagreementReason;
    
}