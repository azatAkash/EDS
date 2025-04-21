package com.student.edsbackend.features.management.repository;

import com.student.edsbackend.features.management.ManagementPlanAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository interface for ManagementPlanAction entities
 */
@Repository
public interface ManagementPlanActionRepository extends JpaRepository<ManagementPlanAction, Integer> {
    
    /**
     * Find a management plan action by ID that is not deleted
     * @param id the ID of the management plan action
     * @return an Optional containing the management plan action if found
     */
    Optional<ManagementPlanAction> findByIdAndIsDeletedFalse(Integer id);
    
    /**
     * Find all management plan actions that are not deleted
     * @return a list of all management plan actions that are not deleted
     */
    List<ManagementPlanAction> findByIsDeletedFalse();

   
}