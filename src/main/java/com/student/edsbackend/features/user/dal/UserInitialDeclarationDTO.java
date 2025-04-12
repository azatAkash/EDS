package com.student.edsbackend.features.user.dal;

import com.student.edsbackend.features.enums.UserDeclarationStatus;
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
    private Integer userId;
    private String userName; // User's full name for display purposes
    private Integer declarationId;
    private String declarationTitle; // Declaration title for display purposes
    private LocalDateTime creationDate;
    private UserDeclarationStatus status;
    private Integer responsibleId;
    private String responsibleName; // Responsible person's name for display purposes
    private Boolean isDeleted;
}
