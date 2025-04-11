package com.student.edsbackend.features.declaration.initial.controller;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestion;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestionDTO;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestionRequestDTO;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestionUpdateDTO;
import com.student.edsbackend.features.declaration.initial.service.InitialDeclarationQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/declarations/questions")
public class InitialDeclarationQuestionController {

    private final InitialDeclarationQuestionService questionService;

    /**
     * Get all non-deleted questions
     */
    @GetMapping
    public ResponseEntity<List<InitialDeclarationQuestionDTO>> getAllQuestions() {
        List<InitialDeclarationQuestionDTO> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    /**
     * Get all non-deleted questions for a specific declaration
     */
    @GetMapping("/by-declaration/{declarationId}")
    public ResponseEntity<List<InitialDeclarationQuestionDTO>> getQuestionsByDeclarationId(@PathVariable Integer declarationId) {
        List<InitialDeclarationQuestionDTO> questions = questionService.getQuestionsByDeclarationId(declarationId);
        return ResponseEntity.ok(questions);
    }

    /**
     * Get a specific question by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<InitialDeclarationQuestionDTO> getQuestionById(@PathVariable Integer id) {
        InitialDeclarationQuestionDTO question = questionService.getQuestionById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Question not found with id: " + id));
        return ResponseEntity.ok(question);
    }

    /**
     * Create a new question
     */
    @PostMapping
    public ResponseEntity<InitialDeclarationQuestionDTO> createQuestion(@RequestBody InitialDeclarationQuestionRequestDTO questionDTO) {
        InitialDeclarationQuestionDTO createdQuestion = questionService.createQuestion(questionDTO);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    /**
     * Update an existing non-deleted question
     */
    @PutMapping("/{id}")
    public ResponseEntity<InitialDeclarationQuestionDTO> updateQuestion(
            @PathVariable Integer id,
            @RequestBody InitialDeclarationQuestionUpdateDTO questionDTO) {
        try {
            InitialDeclarationQuestionDTO updatedQuestion = questionService.updateQuestion(id, questionDTO);
            return ResponseEntity.ok(updatedQuestion);
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                // This is thrown when trying to update a deleted question
                throw e;
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found with id: " + id);
        }
    }

    /**
     * Soft delete a question by setting isDeleted to true
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteQuestion(@PathVariable Integer id) {
        try {
            questionService.deleteQuestion(id);
            return ResponseEntity.ok(new ApiResponse("Question was successfully marked as deleted"));
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found with id: " + id);
        }
    }
}