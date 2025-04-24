package com.student.edsbackend.features.declaration.adhoc.controller;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeRequestDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeUpdateDTO;
import com.student.edsbackend.features.declaration.adhoc.service.UserAdHocExcludeService;
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
 * Controller for managing user ad hoc exclusions
 * Endpoints are accessible to super admin, admin, managers, and users assigned
 * to the exclusion
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/adhoc-declarations/exclude")
public class UserAdHocExcludeController {

    private final UserAdHocExcludeService adHocExcludeService;

    /**
     * Create a new ad hoc exclusion
     * 
     * @param userId     The ID of the user creating the exclusion
     * @param requestDTO The request data for creating the exclusion
     * @return The created UserAdHocExcludeDTO
     */
    @PostMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<UserAdHocExcludeDTO> createAdHocExclusion(
            @PathVariable Integer userId,
            @Valid @RequestBody UserAdHocExcludeRequestDTO requestDTO) {
        UserAdHocExcludeDTO createdExclusion = adHocExcludeService.createAdHocExclusion(userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExclusion);
    }

    /**
     * Get an ad hoc exclusion by ID
     * Accessible to super admin, admin, managers, and users assigned to the
     * exclusion
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<UserAdHocExcludeDTO> getAdHocExclusionById(@PathVariable Integer id) {
        return adHocExcludeService.getAdHocExclusionById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad hoc exclusion not found"));
    }

    /**
     * Get an ad hoc exclusion by ID
     * Accessible to super admin, admin, managers, and users assigned to the
     * exclusion
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<List<UserAdHocExcludeDTO>> getAlltAdHocExclusions() {
        List<UserAdHocExcludeDTO> declarations = adHocExcludeService.getAllAdHocDeclarations();

        return ResponseEntity.ok(declarations);
    }

    /**
     * Get all ad hoc exclusions for a specific user
     * 
     * @param userId The ID of the user to get exclusions for
     * @return List of UserAdHocExcludeDTO objects
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserAdHocExcludeDTO>> getAdHocExclusionsByUserId(@PathVariable Integer userId) {
        List<UserAdHocExcludeDTO> exclusions = adHocExcludeService.getAdHocExclusionsByUserId(userId);
        return ResponseEntity.ok(exclusions);
    }

    /**
     * Get all ad hoc exclusions with a specific status
     * 
     * @param status The status to filter by
     * @return List of UserAdHocExcludeDTO objects
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<List<UserAdHocExcludeDTO>> getAdHocExclusionsByStatus(
            @PathVariable UserDeclarationStatus status) {
        List<UserAdHocExcludeDTO> exclusions = adHocExcludeService.getAdHocExclusionsByStatus(status);
        return ResponseEntity.ok(exclusions);
    }

    /**
     * Update an ad hoc exclusion (partial update - PATCH)
     * 
     * @param id        The ID of the exclusion to update
     * @param updateDTO The data to update
     * @return The updated UserAdHocExcludeDTO
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<UserAdHocExcludeDTO> updateAdHocExclusion(
            @PathVariable Integer id,
            @Valid @RequestBody UserAdHocExcludeUpdateDTO updateDTO) {
        UserAdHocExcludeDTO updatedExclusion = adHocExcludeService.updateAdHocExclusion(id, updateDTO);
        return ResponseEntity.ok(updatedExclusion);
    }

    /**
     * Delete an ad hoc exclusion
     * 
     * @param id The ID of the exclusion to delete
     * @return Success message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse> deleteAdHocExclusion(@PathVariable Integer id) {
        adHocExcludeService.deleteAdHocExclusion(id);
        return ResponseEntity.ok(new ApiResponse("Ad hoc exclusion deleted successfully"));
    }
}