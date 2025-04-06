package com.student.edsbackend.features.declaration.initial;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserInitialDeclaration;
/**
 * Entity representing the initial_declarations table.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "initial_declarations")
public class InitialDeclaration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "activation_date")
    private LocalDateTime activationDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "declaration")
    private List<InitialDeclarationQuestion> questions;

    @OneToMany(mappedBy = "declaration")
    private List<UserInitialDeclaration> userDeclarations;
}