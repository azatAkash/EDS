package com.student.edsbackend.features.declaration.initial.controller;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOptionDTO;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOptionRequestDTO;
import com.student.edsbackend.features.declaration.initial.service.InitialDeclarationOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for managing Initial Declaration Options
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/declarations/options")
public class InitialDeclarationOptionController {

    private final InitialDeclarationOptionService optionService;

    /**
     * Get a specific option by ID
     * @param id The ID of the option to retrieve
     * @return The option DTO if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<InitialDeclarationOptionDTO> getOptionById(@PathVariable Integer id) {
        InitialDeclarationOptionDTO option = optionService.getOptionById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Option not found with id: " + id));
        return ResponseEntity.ok(option);
    }

    /**
     * Create a new option
     * @param optionDTO The option data to create
     * @return The created option DTO
     */
    @PostMapping
    public ResponseEntity<InitialDeclarationOptionDTO> createOption(@RequestBody InitialDeclarationOptionRequestDTO optionDTO) {
        InitialDeclarationOptionDTO createdOption = optionService.createOption(optionDTO);
        return new ResponseEntity<>(createdOption, HttpStatus.CREATED);
    }

    /**
     * Soft delete an option by setting isDeleted to true
     * @param id The ID of the option to delete
     * @return Response indicating success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteOption(@PathVariable Integer id) {
        try {
            optionService.deleteOption(id);
            return ResponseEntity.ok(new ApiResponse("Option was successfully marked as deleted"));
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Option not found with id: " + id);
        }
    }
}