package com.student.edsbackend.dal.declaration;

import com.student.edsbackend.dal.user.UserAdHocDeclare;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing the user_ad_hoc_declare_answers table.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_ad_hoc_declare_answers")
public class UserAdHocDeclareAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_ad_hoc_declare_id", nullable = false)
    private UserAdHocDeclare userAdHocDeclare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private AdHocCategory category;

    @Column(name = "other_category", columnDefinition = "text")
    private String otherCategory;

    @Column(name = "conflict_description", columnDefinition = "text")
    private String conflictDescription;
}