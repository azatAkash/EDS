package com.student.edsbackend.features.declaration.answers.service;

import com.student.edsbackend.features.declaration.answers.UserAdHocDeclareAnswer;
import com.student.edsbackend.features.declaration.answers.dto.UserAdHocDeclareAnswerDTO;

import java.util.List;

/**
 * Service interface for managing user ad hoc declaration answers
 */
public interface UserAdHocDeclareAnswerService {
    
    /**
     * Save a user ad hoc declaration answer
     * 
     * @param answerDTO The DTO containing the answer data to save
     * @return The saved answer as a DTO
     */
    UserAdHocDeclareAnswerDTO saveAnswer(UserAdHocDeclareAnswerDTO answerDTO);
    
    /**
     * Get an answer by its ID
     * 
     * @param id The ID of the answer to retrieve
     * @return The answer as a DTO
     */
    UserAdHocDeclareAnswerDTO getAnswerById(Integer id);
    
    /**
     * Get all answers for a specific ad hoc declaration
     * 
     * @param userAdHocDeclareId The ID of the ad hoc declaration
     * @return List of answers as DTOs
     */
    List<UserAdHocDeclareAnswerDTO> getAnswersByUserAdHocDeclareId(Integer userAdHocDeclareId);
    
    /**
     * Update an existing answer
     * 
     * @param id The ID of the answer to update
     * @param answerDTO The DTO containing the updated data
     * @return The updated answer as a DTO
     */
    UserAdHocDeclareAnswerDTO updateAnswer(Integer id, UserAdHocDeclareAnswerDTO answerDTO);
    
    /**
     * Delete an answer by its ID
     * 
     * @param id The ID of the answer to delete
     */
    void deleteAnswer(Integer id);
    
    /**
     * Get all answers for the current authenticated user's ad hoc declarations
     * 
     * @return List of answers as DTOs
     */
    List<UserAdHocDeclareAnswerDTO> getCurrentUserAnswers();
}