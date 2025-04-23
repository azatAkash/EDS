package com.student.edsbackend.features.declaration.adhoc.repository;

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
     * Find all active ad hoc exclusions for a specific user
     * @param user The user to find exclusions for
     * @return List of UserAdHocExclude entities
     */
    List<UserAdHocExclude> findByUserAndIsDeletedFalse(User user);
    
    /**
     * Find an active ad hoc exclusion by ID
     * @param id The ID of the exclusion to find
     * @return Optional containing the UserAdHocExclude if found
     */
    Optional<UserAdHocExclude> findByIdAndIsDeletedFalse(Integer id);
    
    /**
     * Find all active ad hoc exclusions with a specific status
     * @param status The status to filter by
     * @return List of UserAdHocExclude entities
     */
    List<UserAdHocExclude> findByStatusAndIsDeletedFalse(UserDeclarationStatus status);
    
    /**
     * Find all active ad hoc exclusions for a specific user and status
     * @param user The user to find exclusions for
     * @param status The status to filter by
     * @return List of UserAdHocExclude entities
     */
    List<UserAdHocExclude> findByUserAndStatusAndIsDeletedFalse(User user, UserDeclarationStatus status);
    
    /**
     * Check if a user has any active ad hoc exclusions
     * @param userId The ID of the user to check
     * @return true if the user has active exclusions, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM UserAdHocExclude e WHERE e.user.id = :userId AND e.isDeleted = false")
    boolean hasActiveExclusions(@Param("userId") Integer userId);
}