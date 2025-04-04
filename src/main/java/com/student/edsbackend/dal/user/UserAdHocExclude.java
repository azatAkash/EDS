package com.student.edsbackend.dal.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing the user_ad_hoc_excludes table.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_ad_hoc_excludes")
public class UserAdHocExclude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initial_dec_id")
    private UserInitialDeclaration userInitialDeclaration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ad_hoc_declare_id")
    private UserAdHocDeclare userAdHocDeclare;

    @Column(name = "exclude_reason", columnDefinition = "text")
    private String excludeReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
