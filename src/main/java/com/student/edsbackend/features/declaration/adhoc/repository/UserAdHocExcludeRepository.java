package com.student.edsbackend.features.declaration.adhoc.repository;

import com.student.edsbackend.features.declaration.adhoc.UserAdHocDeclare;
import com.student.edsbackend.features.declaration.adhoc.UserAdHocExclude;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserAdHocExclude entity
 */
@Repository
public interface UserAdHocExcludeRepository extends JpaRepository<UserAdHocExclude, Integer> {
    
    /**
     * Find an exclude by ID where it's not deleted
     */
    Optional<UserAdHocExclude> findByIdAndIsDeletedFalse(Integer id);
    
    /**
     * Find all excludes for a specific user that are not deleted
     */
    List<UserAdHocExclude> findByUserIdAndIsDeletedFalse(Integer userId);
    
    /**
     * Find all excludes for a specific user with a specific status that are not deleted
     */
    List<UserAdHocExclude> findByUserIdAndStatusAndIsDeletedFalse(Integer userId, UserDeclarationStatus status);
    
    /**
     * Find all excludes for a specific initial declaration that are not deleted
     */
    List<UserAdHocExclude> findByUserInitialDeclarationIdAndIsDeletedFalse(Integer initialDeclarationId);
    
    /**
     * Find all excludes for a specific ad hoc declaration that are not deleted
     */
    List<UserAdHocExclude> findByUserAdHocDeclareIdAndIsDeletedFalse(Integer adHocDeclareId);
    
    /**
     * Check if an exclude exists for a specific user and ad hoc declaration that is not deleted
     */
    boolean existsByUserIdAndUserAdHocDeclareIdAndIsDeletedFalse(Integer userId, Integer adHocDeclareId);
    
    /**
     * Check if an exclude exists for a specific user and initial declaration that is not deleted
     */
    boolean existsByUserIdAndUserInitialDeclarationIdAndIsDeletedFalse(Integer userId, Integer initialDeclarationId);
}