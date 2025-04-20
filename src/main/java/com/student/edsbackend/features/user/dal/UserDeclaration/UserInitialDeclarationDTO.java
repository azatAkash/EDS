package com.student.edsbackend.features.user.dal.UserDeclaration;

import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for transferring UserInitialDeclaration data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInitialDeclarationDTO {

    private Integer id;
    private UserDTO user;
    private Integer declarationId;
    private String declarationTitle; // Declaration title for display purposes
    private LocalDateTime creationDate;
    private UserDeclarationStatus status;
    private UserDTO responsible;
    private UserDTO createdBy;
    private String responsibleName; // Responsible person's name for display purposes
    private Boolean isDeleted;
}
