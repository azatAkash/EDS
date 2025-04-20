package com.student.edsbackend.features.declaration.agreement;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.student.edsbackend.configs.JsonConverter;

/**
 * Entity representing the dec_agreement_statements table.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dec_agreement_statements")
public class DecAgreementStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = JsonConverter.class)
    @Column(name = "description", nullable = false, columnDefinition = "json")
    private Map<String, String> description; // {en, ru, kz}

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}