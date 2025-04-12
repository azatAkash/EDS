package com.student.edsbackend.features.declaration.initial.controller;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOptionDTO;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOptionRequestDTO;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOptionUpdateDTO;
import com.student.edsbackend.features.declaration.initial.service.AdditionalAnswerOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for managing Additional Answer Options
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/initial-declarations/additional-options")
public class AdditionalAnswerOptionController {

    private final AdditionalAnswerOptionService additionalAnswerOptionService;

    /**
     * Get a specific additional answer option by ID
     *
     * @param id The ID of the additional answer option to retrieve
     * @return The additional answer option DTO if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdditionalAnswerOptionDTO> getAdditionalAnswerOptionById(@PathVariable Integer id) {
        AdditionalAnswerOptionDTO option = additionalAnswerOptionService.getAdditionalAnswerOptionById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Additional answer option not found with id: " + id));
        return ResponseEntity.ok(option);
    }

    /**
     * Create a new additional answer option
     *
     * @param requestDTO The additional answer option data to create
     * @return The created additional answer option DTO
     */
    @PostMapping
    public ResponseEntity<AdditionalAnswerOptionDTO> createAdditionalAnswerOption(
            @RequestBody AdditionalAnswerOptionRequestDTO requestDTO) {
        AdditionalAnswerOptionDTO createdOption = additionalAnswerOptionService.createAdditionalAnswerOption(requestDTO);
        return new ResponseEntity<>(createdOption, HttpStatus.CREATED);
    }

    /**
     * Update an existing additional answer option
     *
     * @param id The ID of the additional answer option to update
     * @param requestDTO The updated additional answer option data
     * @return The updated additional answer option DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdditionalAnswerOptionDTO> updateAdditionalAnswerOption(
            @PathVariable Integer id,
            @RequestBody AdditionalAnswerOptionUpdateDTO requestDTO) {
        AdditionalAnswerOptionDTO updatedOption = additionalAnswerOptionService.updateAdditionalAnswerOption(id, requestDTO);
        return ResponseEntity.ok(updatedOption);
    }

    /**
     * Soft delete an additional answer option by setting isDeleted to true
     *
     * @param id The ID of the additional answer option to delete
     * @return Response indicating success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAdditionalAnswerOption(@PathVariable Integer id) {
        try {
            additionalAnswerOptionService.deleteAdditionalAnswerOption(id);
            return ResponseEntity.ok(new ApiResponse("Additional answer option was successfully marked as deleted"));
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Additional answer option not found with id: " + id);
        }
    }
}
