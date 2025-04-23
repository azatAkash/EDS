package com.student.edsbackend.features.declaration.adhoc;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.student.edsbackend.configs.JsonConverter;
import com.student.edsbackend.configs.ListOfMapConverter;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclaration;

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
    @JoinColumn(name = "user_initial_dec_id")
    private UserInitialDeclaration userInitialDeclaration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ad_hoc_declare_id")
    private UserAdHocDeclare userAdHocDeclare;

    @Column(name = "exclude_reason", columnDefinition = "text")
    private String excludeReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserDeclarationStatus status;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed;
    
    @Column(name = "has_agreed_with_statements", nullable = false)
    private Boolean hasAgreedWithStatements;

    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = ListOfMapConverter.class)
    @Column(name = "agreed_statements", columnDefinition = "json")
    private List<Map<String, String>> agreedStatements;
}
