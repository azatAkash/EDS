package com.student.edsbackend.features.declaration.adhoc.controller;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareRequestDTO;
import com.student.edsbackend.features.declaration.adhoc.service.UserAdHocDeclareService;
import com.student.edsbackend.features.enums.UserDeclarationStatus;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing user ad hoc declarations
 * Endpoints are accessible to super admin, admin, managers, and users assigned to the declaration
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/adhoc-declarations/declare")
public class UserAdHocDeclareController {

    private final UserAdHocDeclareService adHocDeclareService;

    /**
     * Create a new ad hoc declaration
     * @param userId The ID of the user creating the declaration
     * @param requestDTO The request data for creating the declaration
     * @return The created UserAdHocDeclareDTO
     */
    @PostMapping("/{userId}")
    public ResponseEntity<UserAdHocDeclareDTO> createAdHocDeclaration(
            @PathVariable Integer userId,
            @Valid @RequestBody UserAdHocDeclareRequestDTO requestDTO) {
        UserAdHocDeclareDTO createdDeclaration = adHocDeclareService.createAdHocDeclaration(userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDeclaration);
    }

    /**
     * Get an ad hoc declaration by ID
     * Accessible to super admin, admin, managers, and users assigned to the declaration
     */
    @GetMapping("/declarations/{id}")
    public ResponseEntity<UserAdHocDeclareDTO> getAdHocDeclarationById(@PathVariable Integer id) {
        UserAdHocDeclareDTO declaration = adHocDeclareService.getAdHocDeclarationById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ad hoc declaration with id " + id + " not found"));
        return ResponseEntity.ok(declaration);
    }

    /**
     * Get all ad hoc declarations for a specific user
     * @param userId The ID of the user to find declarations for
     * @return List of UserAdHocDeclareDTO objects
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserAdHocDeclareDTO>> getAdHocDeclarationsByUserId(@PathVariable Integer userId) {
        List<UserAdHocDeclareDTO> declarations = adHocDeclareService.getAdHocDeclarationsByUserId(userId);
        return ResponseEntity.ok(declarations);
    }

    /**
     * Get the latest ad hoc declaration for a user
     * @param userId The ID of the user
     * @return The latest UserAdHocDeclareDTO if found
     */
    @GetMapping("/{userId}/latest")
    public ResponseEntity<UserAdHocDeclareDTO> getLatestAdHocDeclarationByUserId(@PathVariable Integer userId) {
        UserAdHocDeclareDTO declaration = adHocDeclareService.getLatestAdHocDeclarationByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No ad hoc declarations found for user with id " + userId));
        return ResponseEntity.ok(declaration);
    }


    /**
     * Update the status of an ad hoc declaration
     * Accessible to super admin, admin, and managers only
     * @param id The ID of the declaration to update
     * @param status The new status
     * @param responsibleUserId The ID of the user responsible for the update
     * @return The updated UserAdHocDeclareDTO
     */
    @PatchMapping("/status/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<UserAdHocDeclareDTO> updateAdHocDeclarationStatus(
            @PathVariable Integer id,
            @RequestParam UserDeclarationStatus status,
            @RequestParam Integer responsibleUserId) {
        UserAdHocDeclareDTO updatedDeclaration = adHocDeclareService.updateAdHocDeclarationStatus(
                id, status, responsibleUserId);
        return ResponseEntity.ok(updatedDeclaration);
    }

    /**
     * Delete an ad hoc declaration (soft delete)
     * Accessible to super admin, admin, and managers only
     * @param id The ID of the declaration to delete
     * @return Success response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse> deleteAdHocDeclaration(@PathVariable Integer id) {
        adHocDeclareService.deleteAdHocDeclaration(id);
        return ResponseEntity.ok(new ApiResponse("Ad hoc declaration deleted successfully"));
    }
}