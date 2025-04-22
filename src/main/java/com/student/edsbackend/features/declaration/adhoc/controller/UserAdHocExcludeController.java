package com.student.edsbackend.features.declaration.adhoc.controller;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeRequestDTO;
import com.student.edsbackend.features.declaration.adhoc.service.UserAdHocExcludeService;
import com.student.edsbackend.features.enums.UserDeclarationStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing UserAdHocExclude entities
 */
@RestController
@RequestMapping("/api/v1/exclusions")
@RequiredArgsConstructor
public class UserAdHocExcludeController {

    private final UserAdHocExcludeService userAdHocExcludeService;

    /**
     * Create a new exclusion
     * 
     * @param requestDTO the request containing exclusion details
     * @return the created exclusion
     */
    @PostMapping
    public ResponseEntity<UserAdHocExcludeDTO> createExclusion(@RequestBody UserAdHocExcludeRequestDTO requestDTO) {
        UserAdHocExcludeDTO exclusion = userAdHocExcludeService.createExclusion(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(exclusion);
    }

    /**
     * Get an exclusion by ID
     * 
     * @param id the exclusion ID
     * @return the exclusion
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserAdHocExcludeDTO> getExclusionById(@PathVariable Integer id) {
        UserAdHocExcludeDTO exclusion = userAdHocExcludeService.getExclusionById(id);
        return ResponseEntity.ok(exclusion);
    }

    /**
     * Get all exclusions for the current authenticated user
     * 
     * @return list of exclusions
     */
    @GetMapping("/current-user")
    public ResponseEntity<List<UserAdHocExcludeDTO>> getCurrentUserExclusions() {
        List<UserAdHocExcludeDTO> exclusions = userAdHocExcludeService.getCurrentUserExclusions();
        return ResponseEntity.ok( exclusions);
    }

    /**
     * Get all exclusions for a specific user
     * 
     * @param userId the user ID
     * @return list of exclusions
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserAdHocExcludeDTO>> getUserExclusions(@PathVariable Integer userId) {
        List<UserAdHocExcludeDTO> exclusions = userAdHocExcludeService.getUserExclusions(userId);
        return ResponseEntity.ok(exclusions);
    }

    /**
     * Get all exclusions for a specific user with a specific status
     * 
     * @param userId the user ID
     * @param status the exclusion status
     * @return list of exclusions
     */
    @GetMapping("/user/{userId}/status/{status}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserAdHocExcludeDTO>> getUserExclusionsByStatus(
            @PathVariable Integer userId, @PathVariable UserDeclarationStatus status) {
        List<UserAdHocExcludeDTO> exclusions = userAdHocExcludeService.getUserExclusionsByStatus(userId, status);
        return ResponseEntity.ok(exclusions);
    }

    /**
     * Get all exclusions for a specific initial declaration
     * 
     * @param initialDeclarationId the initial declaration ID
     * @return list of exclusions
     */
    @GetMapping("/initial-declaration/{initialDeclarationId}")
    public ResponseEntity<List<UserAdHocExcludeDTO>> getExclusionsByInitialDeclarationId(
            @PathVariable Integer initialDeclarationId) {
        List<UserAdHocExcludeDTO> exclusions = userAdHocExcludeService.getExclusionsByInitialDeclarationId(initialDeclarationId);
        return ResponseEntity.ok(exclusions);
    }

    /**
     * Get all exclusions for a specific ad hoc declaration
     * 
     * @param adHocDeclareId the ad hoc declaration ID
     * @return list of exclusions
     */
    @GetMapping("/ad-hoc-declare/{adHocDeclareId}")
    public ResponseEntity<List<UserAdHocExcludeDTO>> getExclusionsByAdHocDeclareId(
            @PathVariable Integer adHocDeclareId) {
        List<UserAdHocExcludeDTO> exclusions = userAdHocExcludeService.getExclusionsByAdHocDeclareId(adHocDeclareId);
        return ResponseEntity.ok(exclusions);
    }

    /**
     * Update an existing exclusion
     * 
     * @param id the exclusion ID
     * @param requestDTO the request containing updated exclusion details
     * @return the updated exclusion
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserAdHocExcludeDTO> updateExclusion(
            @PathVariable Integer id, @RequestBody UserAdHocExcludeRequestDTO requestDTO) {
        UserAdHocExcludeDTO exclusion = userAdHocExcludeService.updateExclusion(id, requestDTO);
        return ResponseEntity.ok(exclusion);
    }

    /**
     * Confirm an exclusion (mark as confirmed)
     * 
     * @param id the exclusion ID
     * @return the updated exclusion
     */
    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<UserAdHocExcludeDTO> confirmExclusion(@PathVariable Integer id) {
        UserAdHocExcludeDTO exclusion = userAdHocExcludeService.confirmExclusion(id);
        return ResponseEntity.ok(exclusion);
    }

    /**
     * Delete an exclusion (soft delete)
     * 
     * @param id the exclusion ID
     * @return success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteExclusion(@PathVariable Integer id) {
        boolean deleted = userAdHocExcludeService.deleteExclusion(id);
        return ResponseEntity.ok(new ApiResponse("Exclusion deleted successfully"));
    }

    /**
     * Check if a conflict exists for a specific user and ad hoc declaration
     * 
     * @param userId the user ID
     * @param adHocDeclareId the ad hoc declaration ID
     * @return true if a conflict exists
     */
    @GetMapping("/check-conflict/ad-hoc/{userId}/{adHocDeclareId}")
    public ResponseEntity<ApiResponse> checkConflictExistsForAdHocDeclare(
            @PathVariable Integer userId, @PathVariable Integer adHocDeclareId) {
        boolean conflictExists = userAdHocExcludeService.conflictExistsForAdHocDeclare(userId, adHocDeclareId);
        return ResponseEntity.ok(new ApiResponse( "Conflict check completed, conflict exists: " +conflictExists));
    }

    /**
     * Check if a conflict exists for a specific user and initial declaration
     * 
     * @param userId the user ID
     * @param initialDeclarationId the initial declaration ID
     * @return true if a conflict exists
     */
    @GetMapping("/check-conflict/initial/{userId}/{initialDeclarationId}")
    public ResponseEntity<ApiResponse> checkConflictExistsForInitialDeclaration(
            @PathVariable Integer userId, @PathVariable Integer initialDeclarationId) {
        boolean conflictExists = userAdHocExcludeService.conflictExistsForInitialDeclaration(userId, initialDeclarationId);
        return ResponseEntity.ok(new ApiResponse("Conflict check completed, conflict exists: " + conflictExists));
    }
}