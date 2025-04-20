package com.student.edsbackend.features.declaration.agreement.dto;

import java.util.Map;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DecAgreementStatementRequestDTO {
    private Map<String, String> description;
}