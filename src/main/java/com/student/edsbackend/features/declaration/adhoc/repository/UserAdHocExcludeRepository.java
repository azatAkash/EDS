package com.student.edsbackend.features.declaration.adhoc.repository;

import com.student.edsbackend.features.declaration.adhoc.UserAdHocDeclare;
import com.student.edsbackend.features.declaration.adhoc.UserAdHocExclude;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for accessing UserAdHocExclude entities
 */
@Repository
public interface UserAdHocExcludeRepository extends JpaRepository<UserAdHocExclude, Integer> {
    
    /**
     * Find all exclusions for a specific ad hoc declaration
     * @param userAdHocDeclare The ad hoc declaration to find exclusions for
     * @return List of UserAdHocExclude entities
     */
    List<UserAdHocExclude> findByUserAdHocDeclareAndIsDeletedFalse(UserAdHocDeclare userAdHocDeclare);
    
    /**
     * Find all exclusions for a specific user
     * @param user The user to find exclusions for
     * @return List of UserAdHocExclude entities
     */
    List<UserAdHocExclude> findByUserAndIsDeletedFalse(User user);
    
    /**
     * Find all exclusions with a specific status
     * @param status The status to filter by
     * @return List of UserAdHocExclude entities
     */
    List<UserAdHocExclude> findByStatusAndIsDeletedFalse(UserDeclarationStatus status);
    
    /**
     * Find a specific exclusion by ID that is not deleted
     * @param id The ID to find
     * @return Optional containing the UserAdHocExclude if found
     */
    Optional<UserAdHocExclude> findByIdAndIsDeletedFalse(Integer id);
}