package com.student.edsbackend.features.management;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.student.edsbackend.features.declaration.adhoc.UserAdHocDeclare;
import com.student.edsbackend.features.enums.ManagementPlanStatus;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclaration;

/**
 * Entity representing the user_management_plans table.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_management_plans")
public class UserManagementPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_declaration_id")
    private UserInitialDeclaration userDeclaration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_hoc_id")
    private UserAdHocDeclare adHoc;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible", nullable = false)
    private User responsible;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ManagementPlanStatus status;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "is_amended")
    private Boolean isAmended;

    @Column(name = "action_required")
    private Boolean actionRequired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_id")
    private ManagementPlanAction action;

    @Column(name = "action_details", columnDefinition = "text")
    private String actionDetails;

    @Column(name = "execution_date")
    private LocalDateTime executionDate;

    @Column(name = "notification_date")
    private LocalDateTime notificationDate;

    @Column(name = "confirmation_date")
    private LocalDateTime confirmationDate;

    @Column(name = "reason_non_execution", columnDefinition = "text")
    private String reasonNonExecution;

    @Column(name = "acknowledged_by_user")
    private Boolean acknowledgedByUser;

    @Column(name = "ensured_by_manager")
    private Boolean ensuredByManager;

    @Column(name = "user_disagreement_reason", columnDefinition = "text")
    private String userDisagreementReason;
}
