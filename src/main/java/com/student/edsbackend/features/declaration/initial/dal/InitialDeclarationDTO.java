package com.student.edsbackend.features.declaration.initial.dal;


import com.student.edsbackend.features.user.dal.UserDTO;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InitialDeclarationDTO {
    private Integer id;
    private String name;
    private LocalDateTime creationDate;
    private LocalDateTime activationDate;
    private Boolean isActive;
    private Boolean isDeleted;
    private UserDTO createdBy;
}
