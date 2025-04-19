package com.student.edsbackend.features.declaration.adhoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.student.edsbackend.features.declaration.adhoc.UserAdHocDeclare;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.enums.UserDeclarationStatus;

/**
 * Repository interface for UserAdHocDeclare entity.
 */
@Repository
public interface UserAdHocDeclareRepository extends JpaRepository<UserAdHocDeclare, Integer> {
    
    /**
     * Find all ad hoc declarations by user.
     *
     * @param user the user
     * @return list of ad hoc declarations
     */
    List<UserAdHocDeclare> findByUser(User user);
    
    /**
     * Find all ad hoc declarations by user and status.
     *
     * @param user the user
     * @param status the status
     * @return list of ad hoc declarations
     */
    List<UserAdHocDeclare> findByUserAndStatus(User user, UserDeclarationStatus status);
    
    /**
     * Find all non-deleted ad hoc declarations by user.
     *
     * @param user the user
     * @param isDeleted the deletion status
     * @return list of ad hoc declarations
     */
    List<UserAdHocDeclare> findByUserAndIsDeleted(User user, Boolean isDeleted);
    
    /**
     * Find ad hoc declaration by id and user.
     *
     * @param id the id
     * @param user the user
     * @return optional of ad hoc declaration
     */
    Optional<UserAdHocDeclare> findByIdAndUser(Integer id, User user);
}