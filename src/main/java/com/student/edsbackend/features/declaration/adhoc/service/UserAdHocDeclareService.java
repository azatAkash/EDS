package com.student.edsbackend.features.declaration.adhoc.service;

import com.student.edsbackend.features.declaration.adhoc.UserAdHocDeclare;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareRequestDTO;
import com.student.edsbackend.features.enums.UserDeclarationStatus;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing UserAdHocDeclare entities
 */
public interface UserAdHocDeclareService {
    
    /**
     * Create a new ad hoc declaration for a user
     * @param userId The ID of the user creating the declaration
     * @param requestDTO The request data for creating the declaration
     * @return The created UserAdHocDeclareDTO
     */
    UserAdHocDeclareDTO createAdHocDeclaration(Integer userId, UserAdHocDeclareRequestDTO requestDTO);
    
    /**
     * Get an ad hoc declaration by ID
     * @param id The ID of the declaration to retrieve
     * @return Optional containing the UserAdHocDeclareDTO if found
     */
    Optional<UserAdHocDeclareDTO> getAdHocDeclarationById(Integer id);
    
    /**
     * Get all ad hoc declarations for a specific user
     * @param userId The ID of the user to find declarations for
     * @return List of UserAdHocDeclareDTO objects
     */
    List<UserAdHocDeclareDTO> getAdHocDeclarationsByUserId(Integer userId);
    
    /**
     * Get all ad hoc declarations with a specific status
     * @param status The status to filter by
     * @return List of UserAdHocDeclareDTO objects
     */
    List<UserAdHocDeclareDTO> getAdHocDeclarationsByStatus(UserDeclarationStatus status);
    
    /**
     * Update the status of an ad hoc declaration
     * @param id The ID of the declaration to update
     * @param status The new status
     * @param responsibleUserId The ID of the user responsible for the update
     * @return The updated UserAdHocDeclareDTO
     */
    UserAdHocDeclareDTO updateAdHocDeclarationStatus(Integer id, UserDeclarationStatus status);
    
    /**
     * Delete an ad hoc declaration (soft delete)
     * @param id The ID of the declaration to delete
     */
    void deleteAdHocDeclaration(Integer id);
    
    /**
     * Get the latest ad hoc declaration for a user
     * @param userId The ID of the user
     * @return Optional containing the latest UserAdHocDeclareDTO if found
     */
    Optional<UserAdHocDeclareDTO> getLatestAdHocDeclarationByUserId(Integer userId);
}