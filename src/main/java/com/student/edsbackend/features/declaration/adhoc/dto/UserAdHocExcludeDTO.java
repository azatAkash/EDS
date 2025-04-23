package com.student.edsbackend.features.declaration.adhoc.dto;

import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
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
    private UserDTO user;
    private UserDTO createdBy;
    private UserDTO responsible;
    private UserInitialDeclarationDTO userInitialDeclaration;
    private UserAdHocDeclareDTO userAdHocDeclare;
    private String excludeReason;
    private LocalDateTime createdAt;
    private UserDeclarationStatus status;
    private Boolean isDeleted;
    private Boolean isConfirmed;
    private Boolean hasAgreedWithStatements;
    private List<Map<String, String>> agreedStatements;
}