package com.student.edsbackend.features.user.dal;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.student.edsbackend.features.declaration.adhoc.UserAdHocExclude;
import com.student.edsbackend.features.declaration.answers.UserDeclarationAnswer;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.management.UserManagementPlan;

/**
 * Entity representing the user_initial_declarations table.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_initial_declarations")
public class UserInitialDeclaration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "declaration_id", nullable = false)
    private InitialDeclaration declaration;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserDeclarationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible")
    private User responsible;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "userDeclaration")
    private List<UserDeclarationAnswer> answers;

    @OneToMany(mappedBy = "userDeclaration")
    private List<UserManagementPlan> managementPlans;

    @OneToMany(mappedBy = "userInitialDeclaration")
    private List<UserAdHocExclude> adHocExcludes;
}