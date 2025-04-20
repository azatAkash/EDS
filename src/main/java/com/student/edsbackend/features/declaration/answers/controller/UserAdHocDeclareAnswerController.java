package com.student.edsbackend.features.declaration.answers.controller;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.declaration.answers.dto.UserAdHocDeclareAnswerDTO;
import com.student.edsbackend.features.declaration.answers.service.UserAdHocDeclareAnswerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user ad hoc declaration answers
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/adhoc-declarations/answers")
@Tag(name = "User Ad Hoc Declaration Answers", description = "Endpoints for managing user ad hoc declaration answers")
public class UserAdHocDeclareAnswerController {

    private final UserAdHocDeclareAnswerService userAdHocDeclareAnswerService;

    @PostMapping
    @Operation(summary = "Create a new ad hoc declaration answer",
            description = "Creates a new answer for an ad hoc declaration. User is identified from the security context.")
    @PreAuthorize("hasAnyAuthority('USER', 'SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<UserAdHocDeclareAnswerDTO> createAnswer(@RequestBody UserAdHocDeclareAnswerDTO answerDTO) {
        UserAdHocDeclareAnswerDTO createdAnswer = userAdHocDeclareAnswerService.saveAnswer(answerDTO);
        return new ResponseEntity<>(createdAnswer, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an ad hoc declaration answer by ID",
            description = "Retrieves a specific ad hoc declaration answer by its ID.")
    @PreAuthorize("hasAnyAuthority('USER', 'SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<UserAdHocDeclareAnswerDTO> getAnswerById(@PathVariable Integer id) {
        UserAdHocDeclareAnswerDTO answer = userAdHocDeclareAnswerService.getAnswerById(id);
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/declaration/{userAdHocDeclareId}")
    @Operation(summary = "Get all answers for a specific ad hoc declaration",
            description = "Retrieves all answers for a specific ad hoc declaration by its ID.")
    @PreAuthorize("hasAnyAuthority('USER', 'SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserAdHocDeclareAnswerDTO>> getAnswersByDeclarationId(
            @PathVariable Integer userAdHocDeclareId) {
        List<UserAdHocDeclareAnswerDTO> answers = userAdHocDeclareAnswerService.getAnswersByUserAdHocDeclareId(userAdHocDeclareId);
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/current-user")
    @Operation(summary = "Get all answers for the current user's ad hoc declarations",
            description = "Retrieves all answers for the current user's ad hoc declarations. User is identified from the security context.")
    @PreAuthorize("hasAnyAuthority('USER', 'SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserAdHocDeclareAnswerDTO>> getCurrentUserAnswers() {
        List<UserAdHocDeclareAnswerDTO> answers = userAdHocDeclareAnswerService.getCurrentUserAnswers();
        return ResponseEntity.ok(answers);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an ad hoc declaration answer",
            description = "Deletes an ad hoc declaration answer by its ID.")
    @PreAuthorize("hasAnyAuthority( 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> deleteAnswer(@PathVariable Integer id) {
        userAdHocDeclareAnswerService.deleteAnswer(id);
        return ResponseEntity.ok(new ApiResponse("Answer deleted successfully"));
    }
}