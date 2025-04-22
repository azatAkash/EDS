package com.student.edsbackend.features.declaration.agreement.dto;

import java.util.Map;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDecAgreementAnswerExcludeDTO {
    private Integer id;
    private Integer userId;
    private Integer adHocExcludeId;
    private Map<Integer, String> agreeIdandDescription;
    private Boolean isAgreed;
}