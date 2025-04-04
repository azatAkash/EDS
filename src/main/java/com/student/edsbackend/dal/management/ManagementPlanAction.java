package com.student.edsbackend.dal.management;

import com.student.edsbackend.dal.user.UserManagementPlan;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Column(name = "text", columnDefinition = "json")
    private String text; // JSON structure for multilingual text {en, ru, kz}

    @OneToMany(mappedBy = "action")
    private List<UserManagementPlan> managementPlans;
}