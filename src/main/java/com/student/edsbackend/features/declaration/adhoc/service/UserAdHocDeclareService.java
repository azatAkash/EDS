package com.student.edsbackend.features.declaration.adhoc.service;

import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareRequestDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareUpdateDTO;
import com.student.edsbackend.features.enums.UserDeclarationStatus;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing UserAdHocDeclare entities.
 */
public interface UserAdHocDeclareService {
    
    /**
     * Find an ad hoc declaration by its ID.
     *
     * @param id the ad hoc declaration ID
     * @return optional containing the ad hoc declaration if found
     */
    Optional<UserAdHocDeclareDTO> findById(Integer id);
    
    /**
     * Get all ad hoc declarations.
     *
     * @return list of all ad hoc declarations
     */
    List<UserAdHocDeclareDTO> getAllAdHocDeclarations();
    
    /**
     * Get all ad hoc declarations for the current user.
     *
     * @return list of ad hoc declarations for the current user
     */
    List<UserAdHocDeclareDTO> getCurrentUserAdHocDeclarations();
    
    /**
     * Get all ad hoc declarations for a specific user.
     *
     * @param userId the user ID
     * @return list of ad hoc declarations for the specified user
     */
    List<UserAdHocDeclareDTO> getUserAdHocDeclarations(Integer userId);
    
    /**
     * Get all ad hoc declarations with a specific status for the current user.
     *
     * @param status the declaration status
     * @return list of ad hoc declarations with the specified status
     */
    List<UserAdHocDeclareDTO> getCurrentUserAdHocDeclarationsByStatus(UserDeclarationStatus status);
    
    /**
     * Create a new ad hoc declaration.
     *
     * @param requestDTO the request DTO containing the declaration data
     * @return the created ad hoc declaration
     */
    UserAdHocDeclareDTO createAdHocDeclaration(UserAdHocDeclareRequestDTO requestDTO);
    
    /**
     * Update the status of an ad hoc declaration.
     *
     * @param id the ad hoc declaration ID
     * @param status the new status
     * @return the updated ad hoc declaration
     */
    UserAdHocDeclareDTO updateAdHocDeclarationStatus(Integer id,  UserAdHocDeclareUpdateDTO updateDTO);
    
    /**
     * Soft delete an ad hoc declaration.
     *
     * @param id the ad hoc declaration ID
     */
    void deleteAdHocDeclaration(Integer id);
}