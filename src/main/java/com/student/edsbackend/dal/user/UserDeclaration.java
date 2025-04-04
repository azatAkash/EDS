package com.student.edsbackend.dal.user;

import com.student.edsbackend.dal.enums.UserDeclarationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Base class for declaration entities (UserInitialDeclaration and UserAdHocDeclare).
 * This is not directly mapped to a table but provides common fields for declaration entities.
 */
@Entity
@MappedSuperclass
@Getter
@Setter
public abstract class UserDeclaration {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserDeclarationStatus status;
}
