package com.student.edsbackend.features.declaration.agreement;

import com.student.edsbackend.features.declaration.answers.UserAdHocDeclareAnswer;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;
import com.student.edsbackend.features.management.UserManagementPlan;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclaration;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationDTO;
import com.student.edsbackend.features.declaration.adhoc.UserAdHocExclude;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "user_dec_agreement_answers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDecAgreementAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_hoc_declare_answer_id")
    private UserAdHocDeclareAnswer userAdHocDeclareAnswer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_hoc_exclude_id")
    private UserAdHocExclude userAdHocExclude;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_statement_id", nullable = false)
    private DecAgreementStatement agreementStatement;

    @Column(name = "is_agreed", nullable = false)
    private Boolean isAgreed;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
