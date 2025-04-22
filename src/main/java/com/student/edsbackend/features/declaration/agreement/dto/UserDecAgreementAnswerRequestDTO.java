package com.student.edsbackend.features.declaration.agreement.dto;

import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDecAgreementAnswerRequestDTO {
    private Integer userId;
    private Integer adHocDeclareAnswerId;
    private Integer adHocExcludeId;
    private List<Integer> agreementStatementIds;
    private Boolean isAgreed;
}