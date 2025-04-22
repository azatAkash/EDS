package com.student.edsbackend.features.declaration.adhoc.dto;

import com.student.edsbackend.features.enums.UserDeclarationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for transferring UserAdHocExclude data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdHocExcludeDTO {
    private Integer id;
    private Integer userId;
    private String userFullName; // Convenience field for UI display
    private Integer initialDeclarationId;
    private Integer userAdHocDeclareId;
    private String excludeReason;
    private LocalDateTime createdAt;
    private UserDeclarationStatus status;
    private Boolean isDeleted;
    private Boolean isConfirmed;
    private Boolean confirmedAgreements;
    private Map<String, String> agreementsDetails;
}