package com.student.edsbackend.features.declaration.adhoc.repository;

import com.student.edsbackend.features.declaration.adhoc.UserAdHocDeclare;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for accessing UserAdHocDeclare entities
 */
@Repository
public interface UserAdHocDeclareRepository extends JpaRepository<UserAdHocDeclare, Integer> {
    
    /**
     * Find all ad hoc declarations for a specific user
     * @param user The user to find declarations for
     * @return List of UserAdHocDeclare entities
     */
    List<UserAdHocDeclare> findByUserAndIsDeletedFalse(User user);
    boolean existsByUserAndStatusAndIsDeletedFalse(User user, UserDeclarationStatus status);

    /**
     * Find all ad hoc declarations with a specific status
     * @param status The status to filter by
     * @return List of UserAdHocDeclare entities
     */
    List<UserAdHocDeclare> findByStatusAndIsDeletedFalse(UserDeclarationStatus status);
    
    /**
     * Find a specific ad hoc declaration by ID that is not deleted
     * @param id The ID to find
     * @return Optional containing the UserAdHocDeclare if found
     */
    Optional<UserAdHocDeclare> findByIdAndIsDeletedFalse(Integer id);
    
    /**
     * Find the latest ad hoc declaration for a specific user
     * @param userId The user ID to find declarations for
     * @return Optional containing the latest UserAdHocDeclare if found
     */
    @Query("SELECT d FROM UserAdHocDeclare d WHERE d.user.id = :userId AND d.isDeleted = false ORDER BY d.createAt DESC")
    Optional<UserAdHocDeclare> findLatestByUserId(@Param("userId") Integer userId);
    Long countByResponsible(User manager);
}