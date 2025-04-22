package com.student.edsbackend.features.declaration.adhoc.dto;

import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for transferring UserAdHocDeclare data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdHocDeclareDTO {
    private Integer id;
    private UserDTO user;
    private LocalDateTime createAt;
    private Boolean isDeleted;
    private UserDTO responsible;
    private UserDTO createdBy;
    private UserDeclarationStatus status;
    private List<UserAdHocExcludeDTO> adHocExcludes;
    private Boolean hasAgreedWithStatements;
    private List<Map<String, String>> statementAgreementStatuses; // Add this line to store statement agreement statuses
}