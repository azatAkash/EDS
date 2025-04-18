package com.student.edsbackend.features.management.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagementPlanActionDTO {
    private Integer id;
    private Map<String, String> description;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
