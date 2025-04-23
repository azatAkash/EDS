package com.student.edsbackend.features.declaration.initial.dal.option;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.student.edsbackend.configs.JsonConverter;
import com.student.edsbackend.features.declaration.answers.UserDeclarationAnswer;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestion;

/**
 * Entity representing the initial_declaration_options table.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "initial_declaration_options")
public class InitialDeclarationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private InitialDeclarationQuestion question;

    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = JsonConverter.class)
    @Column(name = "description", nullable = false, columnDefinition = "json")
    private Map<String, String> description;


    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = JsonConverter.class)
    @Column(name = "additional_answer_description", columnDefinition = "json")
    private Map<String, String> additionalAnswerDescription;

    @Column(name = "multiple_additional_answers")
    private Boolean multipleAdditionalAnswers;

    @Column(name = "is_conflict")
    private Boolean isConflict;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "option")
    private List<AdditionalAnswerOption> additionalAnswerOptions;

    @OneToMany(mappedBy = "option")
    private List<UserDeclarationAnswer> userDeclarationAnswers;
}