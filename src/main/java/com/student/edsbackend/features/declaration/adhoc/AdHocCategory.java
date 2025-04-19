package com.student.edsbackend.features.declaration.adhoc;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.student.edsbackend.configs.JsonConverter;
import com.student.edsbackend.features.declaration.answers.UserAdHocDeclareAnswer;

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

    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = JsonConverter.class)
    @Column(name = "description", columnDefinition = "json")
    private Map<String, String> description; // JSON structure for multilingual text {en, ru, kz}

    @OneToMany(mappedBy = "category")
    private List<UserAdHocDeclareAnswer> userAdHocDeclareAnswers;
}