package com.student.edsbackend.features.management.dto;

import java.util.Map;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagementPlanActionRequestDTO {
    private Map<String, String> description;
}
