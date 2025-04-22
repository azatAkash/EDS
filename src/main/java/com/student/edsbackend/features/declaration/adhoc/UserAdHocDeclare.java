package com.student.edsbackend.features.declaration.adhoc;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.student.edsbackend.configs.ListOfMapConverter;
import com.student.edsbackend.features.declaration.answers.UserAdHocDeclareAnswer;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.management.UserManagementPlan;
import com.student.edsbackend.features.user.dal.User;

/**
 * Entity representing the user_ad_hoc_declare table.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_ad_hoc_declare")
public class UserAdHocDeclare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserDeclarationStatus status;

    @OneToMany(mappedBy = "userAdHocDeclare")
    private List<UserAdHocDeclareAnswer> answers;

    @OneToMany(mappedBy = "userAdHocDeclare")
    private List<UserManagementPlan> managementPlans;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible")
    private User responsible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "userAdHocDeclare")
    private List<UserAdHocExclude> adHocExcludes;

    @Column(name = "has_agreed_with_statements")
    private Boolean hasAgreedWithStatements;


    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = ListOfMapConverter.class)
    @Column(name = "agreed_statements", columnDefinition = "json")
    private List<Map<String, String>> agreedStatements;

}