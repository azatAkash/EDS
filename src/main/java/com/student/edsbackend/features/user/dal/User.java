package com.student.edsbackend.features.user.dal;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.student.edsbackend.features.declaration.adhoc.UserAdHocDeclare;
import com.student.edsbackend.features.declaration.adhoc.UserAdHocExclude;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;
import com.student.edsbackend.features.management.UserManagementPlan;
import com.student.edsbackend.features.token.Token;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "email", length = 500, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 2000, nullable = false)
    private String password;

    @Column(name = "lastname", length = 100, nullable = false)
    private String lastname;

    @Column(name = "firstname", length = 100, nullable = false)
    private String firstname;

    @Column(name = "middlename", length = 100)
    private String middlename;

    @Column(name = "position")
    private String position;

    @Column(name = "department")
    private String department;


    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "registration_date", nullable = false)
    private java.time.LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(mappedBy = "createdBy")
    private List<UserManagementPlan> managementPlans;

    @OneToMany(mappedBy = "user")
    private List<UserAdHocExclude> adHocExcludes;

    @OneToMany(mappedBy = "user")
    private List<UserAdHocDeclare> adHocDeclares;

    @OneToMany(mappedBy = "createdBy")
    private List<InitialDeclaration> initialDecalarations;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
