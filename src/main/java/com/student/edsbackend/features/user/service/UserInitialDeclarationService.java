package com.student.edsbackend.features.user.service;

import java.util.List;
import java.util.Optional;

import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationDTO;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationRequestDTO;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationUpdateDTO;

/**
 * Service interface for managing user initial declarations
 */
public interface UserInitialDeclarationService {

    /**
     * Get a user initial declaration by ID
     *
     * @param id The ID of the user initial declaration
     * @return An Optional containing the user initial declaration if found
     */
    Optional<UserInitialDeclarationDTO> getUserInitialDeclarationById(Integer id);

    /**
     * Get all user initial declarations
     *
     * @return A list of all user initial declarations
     */
    List<UserInitialDeclarationDTO> getAllUserInitialDeclarations();

    /**
     * Create a new user initial declaration
     *
     * @param requestDTO The request DTO containing the data for the new
     * declaration
     * @return The created user initial declaration
     */
    UserInitialDeclarationDTO createUserInitialDeclaration(UserInitialDeclarationRequestDTO requestDTO);

    /**
     * Send a user initial declaration for approval
     *
     * @param id The ID of the user initial declaration to send for approval
     * @return The updated user initial declaration with status set to
     * SENT_FOR_APPROVAL
     */
    UserInitialDeclarationDTO sendForApproval(Integer id);
    
    /**
     * Send the current user's initial declaration for approval
     * Finds the user from the security context, validates that they have a declaration with CREATED status
     * and updates its status to SENT_FOR_APPROVAL
     *
     * @return The updated user initial declaration with status set to SENT_FOR_APPROVAL
     */
    UserInitialDeclarationDTO sendCurrentUserDeclarationForApproval();

    /**
     * Verify a user initial declaration
     *
     * @param id The ID of the user initial declaration to verify
     * @param status The new status to set for the declaration
     * @return The updated user initial declaration with the new status
     */
    UserInitialDeclarationDTO verifyDeclaration(Integer id, UserInitialDeclarationUpdateDTO updateDTO);

    /**
     * Delete a user initial declaration (soft delete)
     *
     * @param id The ID of the user initial declaration to delete
     */
    void deleteUserInitialDeclaration(Integer id);
}
