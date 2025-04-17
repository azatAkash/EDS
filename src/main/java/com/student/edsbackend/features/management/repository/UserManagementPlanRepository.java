package com.student.edsbackend.features.management.repository;

import com.student.edsbackend.features.management.UserManagementPlan;
import com.student.edsbackend.features.user.dal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserManagementPlanRepository extends JpaRepository<UserManagementPlan, Integer> {
    
    /**
     * Find all management plans that are not deleted
     */
    List<UserManagementPlan> findByIsDeletedFalse();
    
    /**
     * Find all management plans for a specific user declaration
     */
    List<UserManagementPlan> findByUserDeclarationIdAndIsDeletedFalse(Integer userDeclarationId);
    
    /**
     * Find all management plans for a specific ad hoc declaration
     */
    List<UserManagementPlan> findByAdHocIdAndIsDeletedFalse(Integer adHocId);
    
    /**
     * Find all management plans created by a specific user
     */
    List<UserManagementPlan> findByCreatedByAndIsDeletedFalse(User createdBy);
    
    /**
     * Find a specific management plan by ID that is not deleted
     */
    Optional<UserManagementPlan> findByIdAndIsDeletedFalse(Integer id);
    
    /**
     * Find all management plans associated with a user (either through user declaration or ad hoc)
     */
    List<UserManagementPlan> findByUserDeclaration_UserIdOrAdHoc_UserIdAndIsDeletedFalse(Integer userId, Integer sameUserId);
}