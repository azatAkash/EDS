package com.student.edsbackend.features.user.dal.UserDeclaration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInitialDeclarationRepository extends JpaRepository<UserInitialDeclaration, Integer> {

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
     * Find all declarations that are not deleted
     */
    List<UserInitialDeclaration> findByIsDeletedFalse();
}
