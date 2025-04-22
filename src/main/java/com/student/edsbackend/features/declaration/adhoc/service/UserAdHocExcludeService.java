package com.student.edsbackend.features.declaration.adhoc.service;

import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeRequestDTO;
import com.student.edsbackend.features.enums.UserDeclarationStatus;

import java.util.List;

/**
 * Service interface for managing UserAdHocExclude entities
 */
public interface UserAdHocExcludeService {
    
    /**
     * Create a new exclusion for a conflict
     * Validates that the conflict actually exists before creating the exclusion
     * 
     * @param requestDTO the request containing exclusion details
     * @return the created exclusion as DTO
     */
    UserAdHocExcludeDTO createExclusion(UserAdHocExcludeRequestDTO requestDTO);
    
    /**
     * Get an exclusion by its ID
     * 
     * @param id the exclusion ID
     * @return the exclusion as DTO
     */
    UserAdHocExcludeDTO getExclusionById(Integer id);
    
    /**
     * Get all exclusions for the current authenticated user
     * 
     * @return list of exclusions as DTOs
     */
    List<UserAdHocExcludeDTO> getCurrentUserExclusions();
    
    /**
     * Get all exclusions for a specific user
     * 
     * @param userId the user ID
     * @return list of exclusions as DTOs
     */
    List<UserAdHocExcludeDTO> getUserExclusions(Integer userId);
    
    /**
     * Get all exclusions for a specific user with a specific status
     * 
     * @param userId the user ID
     * @param status the exclusion status
     * @return list of exclusions as DTOs
     */
    List<UserAdHocExcludeDTO> getUserExclusionsByStatus(Integer userId, UserDeclarationStatus status);
    
    /**
     * Get all exclusions for a specific initial declaration
     * 
     * @param initialDeclarationId the initial declaration ID
     * @return list of exclusions as DTOs
     */
    List<UserAdHocExcludeDTO> getExclusionsByInitialDeclarationId(Integer initialDeclarationId);
    
    /**
     * Get all exclusions for a specific ad hoc declaration
     * 
     * @param adHocDeclareId the ad hoc declaration ID
     * @return list of exclusions as DTOs
     */
    List<UserAdHocExcludeDTO> getExclusionsByAdHocDeclareId(Integer adHocDeclareId);
    
    /**
     * Update an existing exclusion
     * 
     * @param id the exclusion ID
     * @param requestDTO the request containing updated exclusion details
     * @return the updated exclusion as DTO
     */
    UserAdHocExcludeDTO updateExclusion(Integer id, UserAdHocExcludeRequestDTO requestDTO);
    
    /**
     * Confirm an exclusion (mark as confirmed)
     * 
     * @param id the exclusion ID
     * @return the updated exclusion as DTO
     */
    UserAdHocExcludeDTO confirmExclusion(Integer id);
    
    /**
     * Delete an exclusion (soft delete)
     * 
     * @param id the exclusion ID
     * @return true if deleted successfully
     */
    boolean deleteExclusion(Integer id);
    
    /**
     * Check if a conflict exists for a specific user and ad hoc declaration
     * 
     * @param userId the user ID
     * @param adHocDeclareId the ad hoc declaration ID
     * @return true if a conflict exists
     */
    boolean conflictExistsForAdHocDeclare(Integer userId, Integer adHocDeclareId);
    
    /**
     * Check if a conflict exists for a specific user and initial declaration
     * 
     * @param userId the user ID
     * @param initialDeclarationId the initial declaration ID
     * @return true if a conflict exists
     */
    boolean conflictExistsForInitialDeclaration(Integer userId, Integer initialDeclarationId);
}