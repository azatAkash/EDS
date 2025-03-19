package com.student.edsbackend.dal.user;

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
    private String role;
}
