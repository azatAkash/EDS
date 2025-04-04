package com.student.edsbackend.dal.declaration;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity representing the ad_hoc_categories table.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ad_hoc_categories")
public class AdHocCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "description", columnDefinition = "json")
    private String description; // JSON structure for multilingual text {en, ru, kz}

    @OneToMany(mappedBy = "category")
    private List<UserAdHocDeclareAnswer> userAdHocDeclareAnswers;
}