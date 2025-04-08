package com.student.edsbackend.features.token;

import com.student.edsbackend.features.user.dal.User;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a refresh token stored in the database.
 * Only refresh tokens are stored, access tokens are not persisted.
 * Refresh tokens have a 7-day expiration and can only be used once.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_tokens")
public class Token {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "G_TOKEN_ENTITY")
    @SequenceGenerator(allocationSize = 1, name = "G_TOKEN_ENTITY", sequenceName = "SEQ_TOKEN_ENTITY")
    @Column(name = "id")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
