package com.student.edsbackend.features.notifications;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.student.edsbackend.configs.JsonConverter;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOption;
import com.student.edsbackend.features.enums.QuestionType;
import com.student.edsbackend.features.user.dal.User;

/**
 * Entity representing the initial_declaration_questions table.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "platform_notification")
public class platformNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = JsonConverter.class)
    @Column(name = "description", columnDefinition = "json", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}

