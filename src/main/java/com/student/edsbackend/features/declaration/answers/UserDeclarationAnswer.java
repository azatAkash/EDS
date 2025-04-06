package com.student.edsbackend.features.declaration.answers;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.student.edsbackend.features.declaration.initial.InitialDeclarationOption;
import com.student.edsbackend.features.user.dal.UserInitialDeclaration;

/**
 * Entity representing the user_declaration_answers table.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_declaration_answers")
public class UserDeclarationAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_declaration_id", nullable = false)
    private UserInitialDeclaration userDeclaration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private InitialDeclarationOption option;

    @Column(name = "is_answered")
    private Boolean isAnswered;

    @Column(name = "answer", columnDefinition = "text")
    private String answer;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "userDeclarationAnswer")
    private List<UserDeclarationAdditionalAnswer> additionalAnswers;
}