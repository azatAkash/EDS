package com.student.edsbackend.features.declaration.agreement.dto;

import java.util.Map;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DecAgreementStatementUpdateResponseDTO {
    private Integer id;
    private Map<String, String> oldDescription;
    private Map<String, String> newDescription;
    private Boolean isDeleted;
}