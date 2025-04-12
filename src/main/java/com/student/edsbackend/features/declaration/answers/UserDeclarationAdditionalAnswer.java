package com.student.edsbackend.features.declaration.answers;


import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOption;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "user_declaration_additional_answers",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"answer_option_id", "user_declaration_answer_id", "order_index"}
    )
)
public class UserDeclarationAdditionalAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_option_id", nullable = false)
    private AdditionalAnswerOption answerOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_declaration_answer_id", nullable = false)
    private UserDeclarationAnswer userDeclarationAnswer;

    @Column(name = "answer", columnDefinition = "text")
    private String answer;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "order_index", columnDefinition = "smallint", nullable = false)
    private Short orderIndex; // Use Short for smallint in Java
}
