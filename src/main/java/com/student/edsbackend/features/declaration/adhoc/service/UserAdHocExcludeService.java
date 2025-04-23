package com.student.edsbackend.features.declaration.adhoc.service;

import com.student.edsbackend.features.declaration.adhoc.UserAdHocExclude;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeRequestDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeUpdateDTO;
import com.student.edsbackend.features.enums.UserDeclarationStatus;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing UserAdHocExclude entities
 */
public interface UserAdHocExcludeService {
    
    /**
     * Create a new ad hoc exclusion for a user
     * @param userId The ID of the user creating the exclusion
     * @param requestDTO The request data for creating the exclusion
     * @return The created UserAdHocExcludeDTO
     */
    UserAdHocExcludeDTO createAdHocExclusion(Integer userId, UserAdHocExcludeRequestDTO requestDTO);
    
    /**
     * Get an ad hoc exclusion by ID
     * @param id The ID of the exclusion to retrieve
     * @return Optional containing the UserAdHocExcludeDTO if found
     */
    Optional<UserAdHocExcludeDTO> getAdHocExclusionById(Integer id);
    
    /**
     * Get all ad hoc exclusions for a specific user
     * @param userId The ID of the user to get exclusions for
     * @return List of UserAdHocExcludeDTO objects
     */
    List<UserAdHocExcludeDTO> getAdHocExclusionsByUserId(Integer userId);
    
    /**
     * Get all ad hoc exclusions with a specific status
     * @param status The status to filter by
     * @return List of UserAdHocExcludeDTO objects
     */
    List<UserAdHocExcludeDTO> getAdHocExclusionsByStatus(UserDeclarationStatus status);
    
    /**
     * Update an ad hoc exclusion (partial update - PATCH)
     * @param id The ID of the exclusion to update
     * @param updateDTO The data to update
     * @return The updated UserAdHocExcludeDTO
     */
    UserAdHocExcludeDTO updateAdHocExclusion(Integer id, UserAdHocExcludeUpdateDTO updateDTO);
    

    List<UserAdHocExcludeDTO> getAllAdHocDeclarations();
    /**
     * Soft delete an ad hoc exclusion
     * @param id The ID of the exclusion to delete
     */
    void deleteAdHocExclusion(Integer id);
    
    /**
     * Convert a UserAdHocExclude entity to a UserAdHocExcludeDTO
     * @param exclusion The entity to convert
     * @return The converted DTO
     */
    UserAdHocExcludeDTO convertToDTO(UserAdHocExclude exclusion);
}