package com.student.edsbackend.features.user.dal.UserDeclaration;

import com.student.edsbackend.features.enums.UserDeclarationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating only the status of a UserInitialDeclaration
 * Used specifically for PATCH requests to ensure other fields remain non-editable
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInitialDeclarationStatusDTO {

    private UserDeclarationStatus status;
}