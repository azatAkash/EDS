package com.student.edsbackend.dal.user;

import com.student.edsbackend.dal.declaration.InitialDeclaration;
import com.student.edsbackend.dal.declaration.UserDeclarationAnswer;
import com.student.edsbackend.dal.enums.UserDeclarationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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