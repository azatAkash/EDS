package com.student.edsbackend.features.declaration.initial.service;

import java.util.List;
import java.util.Optional;

import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestion;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestionDTO;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestionRequestDTO;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestionUpdateDTO;

public interface InitialDeclarationQuestionService {
    /**
     * Get all non-deleted questions
     * @return List of question DTOs
     */
    List<InitialDeclarationQuestionDTO> getAllQuestions();
    
    /**
     * Get all non-deleted questions for a specific declaration
     * @param declarationId The ID of the declaration
     * @return List of question DTOs
     */
    List<InitialDeclarationQuestionDTO> getQuestionsByDeclarationId(Integer declarationId);
    
    /**
     * Get a specific question by ID if it's not deleted
     * @param id The ID of the question
     * @return Optional containing the question DTO if found and not deleted
     */
    Optional<InitialDeclarationQuestionDTO> getQuestionById(Integer id);
    
    /**
     * Create a new question
     * @param questionDTO The question DTO to create
     * @return The created question as DTO
     */
    InitialDeclarationQuestionDTO createQuestion(InitialDeclarationQuestionRequestDTO questionDTO);
    
    /**
     * Update an existing non-deleted question
     * @param id The ID of the question to update
     * @param questionDTO The updated question data as DTO
     * @return The updated question as DTO
     */
    InitialDeclarationQuestionDTO updateQuestion(Integer id, InitialDeclarationQuestionUpdateDTO questionDTO);
    
    /**
     * Soft delete a question by setting isDeleted to true
     * @param id The ID of the question to delete
     */
    void deleteQuestion(Integer id);
}