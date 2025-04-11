package com.student.edsbackend.features.declaration.initial.dal.declaration_metadata;


import com.student.edsbackend.features.user.dal.UserDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InitialDeclarationRequestDTO {
    private String name;
    private LocalDateTime activationDate;
    private Boolean isActive;
}
