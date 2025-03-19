package com.student.edsbackend.dal.user;

import com.student.edsbackend.dal.declaration.Declaration;
import com.student.edsbackend.dal.token.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "G_USER_ENTITY")
    @SequenceGenerator(allocationSize = 1, name = "G_USER_ENTITY", sequenceName = "SEQ_USER_ENTITY")
    @Column(name = "id")
    private Integer id;
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;
    @Column(name = "password", length = 100, nullable = false)
    private String password;
    @Column(name = "lastname", length = 100, nullable = false)
    private String lastname;
    @Column(name = "firstname", length = 100, nullable = false)
    private String firstname;
    @Column(name = "middlename", length = 100)
    private String middlename;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(mappedBy = "user")
    private List<Declaration> declarations;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
