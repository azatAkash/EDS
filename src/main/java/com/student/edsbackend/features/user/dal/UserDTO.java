package com.student.edsbackend.features.user.dal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {
    private Integer id;
    private String email;
    private String password;  // Added password field
    private String lastname;
    private String firstname;
    private String middlename;
    private Role role;
    private String position;
    private String department;

}
