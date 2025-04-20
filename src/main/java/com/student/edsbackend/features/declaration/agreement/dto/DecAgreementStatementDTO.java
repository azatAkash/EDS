package com.student.edsbackend.features.declaration.agreement.dto;

import java.util.Map;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DecAgreementStatementDTO {
    private Integer id;
    private Map<String, String> description;
    private Boolean isDeleted;
}