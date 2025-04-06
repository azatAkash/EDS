package com.student.edsbackend.features.declaration.initial;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @Column(name = "note", columnDefinition = "text")
    private String note;

    @Column(name = "is_required", nullable = false)
    private Boolean isRequired;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "question")
    private List<InitialDeclarationOption> options;
}