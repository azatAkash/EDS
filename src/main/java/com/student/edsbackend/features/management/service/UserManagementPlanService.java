package com.student.edsbackend.features.management.service;

import com.student.edsbackend.features.management.UserManagementPlan;
import com.student.edsbackend.features.management.dto.UserManagementPlanDTO;
import com.student.edsbackend.features.management.dto.UserManagementPlanRequestDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing UserManagementPlan entities
 */
public interface UserManagementPlanService {
    
    /**
     * Find a management plan by ID
     * @param id the ID of the management plan
     * @return an Optional containing the management plan if found
     */
    Optional<UserManagementPlanDTO> getManagementPlanById(Integer id);
    
    /**
     * Get all management plans
     * @return a list of all management plans
     */
    List<UserManagementPlanDTO> getAllManagementPlans();
    
    /**
     * Get management plans for the current authenticated user
     * @return a list of management plans for the current user
     */
    List<UserManagementPlanDTO> getCurrentUserManagementPlans();
    
    /**
     * Create a new management plan
     * @param requestDTO the request DTO containing the data for the new management plan
     * @return the created management plan
     */
    UserManagementPlanDTO createManagementPlan(UserManagementPlanRequestDTO requestDTO);
    
    /**
     * Delete a management plan by ID
     * @param id the ID of the management plan to delete
     * @return true if the management plan was deleted, false otherwise
     */
    boolean deleteManagementPlan(Integer id);
    
    boolean ammendManagementPlan(Integer id);

    /**
     * Check if the current user has access to the management plan
     * @param managementPlan the management plan to check
     * @return true if the user has access, false otherwise
     */
    boolean hasAccessToManagementPlan(UserManagementPlan managementPlan);
}