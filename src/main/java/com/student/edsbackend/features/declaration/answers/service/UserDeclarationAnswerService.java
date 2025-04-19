package com.student.edsbackend.features.declaration.answers.service;

import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationAnswerRequestDTO;
import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationAnswerResponseDTO;
import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationDetailedResponseDTO;

/**
 * Service interface for managing user declaration answers
 */
public interface UserDeclarationAnswerService {
    
    /**
     * Save user's declaration answers
     * 
     * @param requestDTO The DTO containing the answers to save
     * @return A response DTO with information about the saved answers
     */
    UserDeclarationAnswerResponseDTO saveUserDeclarationAnswers(UserDeclarationAnswerRequestDTO requestDTO);
    
    /**
     * Get user's declaration answers for the current user
     * 
     * @return A response DTO with information about the user's answers
     */
    UserDeclarationDetailedResponseDTO getCurrentUserDeclarationAnswers();

    /**
     * Get user's declaration answer by ID (for admin and super admin only)
     * 
     * @param id The ID of the answer to retrieve
     * @return A response DTO with information about the user's answer
     */
    // UserDeclarationDetailedResponseDTO getUserDeclarationAnswerById(Integer id);

    /**
     * Delete a specific user declaration answer by ID
     * 
     * @param id The ID of the answer to delete
     */
    void deleteUserDeclarationAnswerById(Integer id);

    /**
     * Delete all user answers for the current declaration and current user
     */
    void deleteCurrentUserDeclarationAnswers();
}