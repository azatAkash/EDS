package com.student.edsbackend.dal.declaration;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity representing the additional_answer_options table.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "additional_answer_options")
public class AdditionalAnswerOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private InitialDeclarationOption option;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "is_required")
    private Boolean isRequired;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "answerOption")
    private List<UserDeclarationAdditionalAnswer> userDeclarationAdditionalAnswers;
}