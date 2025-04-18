package com.student.edsbackend.features.declaration.initial.dal.questions;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.student.edsbackend.configs.JsonConverter;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOption;
import com.student.edsbackend.features.enums.QuestionType;

/**
 * Entity representing the initial_declaration_questions table.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "initial_declaration_questions")
public class InitialDeclarationQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "order_number", nullable = false)
    private Short orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "declaration_id", nullable = false)
    private InitialDeclaration declaration;

    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = JsonConverter.class)
    @Column(name = "description", columnDefinition = "json", nullable = false)
    private Map<String, String> description;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @JdbcTypeCode(SqlTypes.JSON)
@Convert(converter = JsonConverter.class)
    @Column(name = "note", columnDefinition = "json")
    private Map<String, String> note;

    @Column(name = "is_required", nullable = false)
    private Boolean isRequired;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}