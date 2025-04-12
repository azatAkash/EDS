package com.student.edsbackend.features.user.dal;

import com.student.edsbackend.features.enums.UserDeclarationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating a UserInitialDeclaration
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInitialDeclarationUpdateDTO {

    private UserDeclarationStatus status;
    private Integer responsibleId;
    private Boolean isDeleted;
}
