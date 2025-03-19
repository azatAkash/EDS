package com.student.edsbackend.dal.declaration;

import com.student.edsbackend.dal.enums.DeclarationStatus;
import com.student.edsbackend.dal.token.TokenType;
import com.student.edsbackend.dal.user.User;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "declarations",
        uniqueConstraints = @UniqueConstraint(columnNames = "user_id")
)

public class Declaration {

    public static Object builder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "G_DECLARATION_ENTITY")
    @SequenceGenerator(allocationSize = 1, name = "G_DECLARATION_ENTITY", sequenceName = "SEQ_DECLARATION_ENTITY")
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "c1", nullable = false, columnDefinition = "json")
    private JsonNode c1;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "c2", nullable = false, columnDefinition = "json")
    private JsonNode c2;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "c3", nullable = false, columnDefinition = "json")
    private JsonNode c3;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "c4", nullable = false, columnDefinition = "json")
    private JsonNode c4;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "c5", nullable = false, columnDefinition = "json")
    private JsonNode c5;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "c6", nullable = false, columnDefinition = "json")
    private JsonNode c6;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "c7", nullable = false, columnDefinition = "json")
    private JsonNode c7;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "c8", nullable = false, columnDefinition = "json")
    private JsonNode c8;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "c9", nullable = false, columnDefinition = "json")
    private JsonNode c9;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "c10", nullable = false, columnDefinition = "json")
    private JsonNode c10;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Manager's user id (foreign key reference)
    @Column(name = "manager_id")
    private Integer managerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeclarationStatus status;
}
