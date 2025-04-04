package com.student.edsbackend.dal.user;

import com.student.edsbackend.dal.declaration.UserAdHocDeclareAnswer;
import com.student.edsbackend.dal.enums.UserDeclarationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "adHoc")
    private List<UserManagementPlan> managementPlans;

    @OneToMany(mappedBy = "userAdHocDeclare")
    private List<UserAdHocExclude> adHocExcludes;
}