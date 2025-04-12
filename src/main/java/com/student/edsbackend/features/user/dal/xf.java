package com.student.edsbackend.features.user.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;
import com.student.edsbackend.features.enums.UserDeclarationStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface xf extends JpaRepository<UserInitialDeclaration, Integer> {

    /**
     * Find a user declaration by user ID and declaration ID where it's not
     * deleted
     */
    Optional<UserInitialDeclaration> findByUserIdAndDeclarationIdAndIsDeletedFalse(Integer userId, Integer declarationId);

    /**
     * Find all declarations for a specific user that are not deleted
     */
    List<UserInitialDeclaration> findByUserIdAndIsDeletedFalse(Integer userId);
    
    /**
     * Find a user declaration by user ID and status where it's not deleted
     */
    Optional<UserInitialDeclaration> findByUserIdAndStatusAndIsDeletedFalse(Integer userId, UserDeclarationStatus status);

    /**
     * Find all declarations that are not deleted
     */
    List<UserInitialDeclaration> findByIsDeletedFalse();
}
