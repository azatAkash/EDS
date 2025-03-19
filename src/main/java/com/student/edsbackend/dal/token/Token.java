package com.student.edsbackend.dal.token;

import com.student.edsbackend.dal.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "G_TOKEN_ENTITY")
    @SequenceGenerator(allocationSize = 1, name = "G_TOKEN_ENTITY", sequenceName = "SEQ_TOKEN_ENTITY")
    @Column(name = "id")
    public Integer id;

    @Column(nullable = false, unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}
