package com.student.edsbackend.features.declaration.agreement.dto;

import java.util.Map;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDecAgreementAnswerDeclareDTO {
    private Integer id;
    private Integer userId;
    private Integer adHocDeclareAnswerId;
    private Map<Integer, String> agreeIdandDescription;
    private Boolean isAgreed;
}