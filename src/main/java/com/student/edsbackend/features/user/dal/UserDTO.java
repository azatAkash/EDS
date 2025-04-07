package com.student.edsbackend.features.user.dal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserDTO {
    private Integer id;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;  // Added password field
    private String lastname;
    private String firstname;
    private String middlename;
    private Role role;
    private String position;
    private String department;
    private Boolean isActive;
    private Boolean isDeleted;
    private LocalDateTime registrationDate;
}
