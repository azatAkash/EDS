package com.student.edsbackend.features.management;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.student.edsbackend.configs.JsonConverter;

/**
 * Entity representing the management_plan_actions table.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "management_plan_actions")
public class ManagementPlanAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JdbcTypeCode(SqlTypes.JSON)
@Convert(converter = JsonConverter.class)
    @Column(name = "description", columnDefinition = "json")
    private Map<String, String> description; // JSON structure for multilingual text {en, ru, kz}

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "action")
    private List<UserManagementPlan> managementPlans;
}