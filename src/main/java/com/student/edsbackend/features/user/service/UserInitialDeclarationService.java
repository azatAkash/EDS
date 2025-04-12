package com.student.edsbackend.features.user.service;

import com.student.edsbackend.features.user.dal.UserInitialDeclarationDTO;
import com.student.edsbackend.features.user.dal.UserInitialDeclarationRequestDTO;
import com.student.edsbackend.features.user.dal.UserInitialDeclarationUpdateDTO;

import java.util.List;
import java.util.Optional;

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
     * Update a user initial declaration
     *
     * @param id The ID of the user initial declaration to update
     * @param updateDTO The update DTO containing the data to update
     * @return The updated user initial declaration
     */
    UserInitialDeclarationDTO updateUserInitialDeclaration(Integer id, UserInitialDeclarationUpdateDTO updateDTO);

    /**
     * Delete a user initial declaration (soft delete)
     *
     * @param id The ID of the user initial declaration to delete
     */
    void deleteUserInitialDeclaration(Integer id);
}
