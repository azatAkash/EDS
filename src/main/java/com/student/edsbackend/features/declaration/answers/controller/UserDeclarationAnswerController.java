package com.student.edsbackend.features.declaration.answers.controller;

import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationAnswerRequestDTO;
import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationAnswerResponseDTO;
import com.student.edsbackend.features.declaration.answers.service.UserDeclarationAnswerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/declaration-answers")
@Tag(name = "User Declaration Answers", description = "Endpoints for managing user declaration answers")
public class UserDeclarationAnswerController {

    private final UserDeclarationAnswerService userDeclarationAnswerService;

    @PostMapping
    @Operation(summary = "Save user declaration answers",
            description = "Saves the user's answers to the initial declaration questions. User is identified from the security context.")
    @PreAuthorize("hasAnyAuthority('USER', 'SUPER_ADMIN')")
    public ResponseEntity<UserDeclarationAnswerResponseDTO> saveUserDeclarationAnswers(
            @RequestBody UserDeclarationAnswerRequestDTO requestDTO) {
        UserDeclarationAnswerResponseDTO responseDTO = userDeclarationAnswerService.saveUserDeclarationAnswers(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get current user's declaration answers",
            description = "Retrieves the current user's answers to the initial declaration questions. User is identified from the security context.")
    @PreAuthorize("hasAnyAuthority('USER', 'SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<UserDeclarationAnswerResponseDTO> getCurrentUserDeclarationAnswers() {
        UserDeclarationAnswerResponseDTO responseDTO = userDeclarationAnswerService.getCurrentUserDeclarationAnswers();
        return ResponseEntity.ok(responseDTO);
    }
}