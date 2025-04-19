package com.student.edsbackend.features.declaration.adhoc.controller;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareRequestDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareUpdateDTO;
import com.student.edsbackend.features.declaration.adhoc.service.UserAdHocDeclareService;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing ad hoc declarations.
 */
@RestController
@RequestMapping("/api/adhoc-declarations/declare")
@RequiredArgsConstructor
public class UserAdHocDeclareController {

    private final UserAdHocDeclareService adHocDeclareService;

    /**
     * Get an ad hoc declaration by ID.
     *
     * @param id the ad hoc declaration ID
     * @return the ad hoc declaration
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<UserAdHocDeclareDTO> getAdHocDeclarationById(@PathVariable Integer id) {
        UserAdHocDeclareDTO adHocDeclaration = adHocDeclareService.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad hoc declaration not found"));
        return ResponseEntity.ok(adHocDeclaration);
    }

    /**
     * Get all ad hoc declarations.
     *
     * @return list of all ad hoc declarations
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserAdHocDeclareDTO>> getAllAdHocDeclarations() {
        List<UserAdHocDeclareDTO> adHocDeclarations = adHocDeclareService.getAllAdHocDeclarations();
        return ResponseEntity.ok(adHocDeclarations);
    }

    /**
     * Get all ad hoc declarations for the current user.
     *
     * @return list of ad hoc declarations for the current user
     */
    @GetMapping("/current-user")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<List<UserAdHocDeclareDTO>> getCurrentUserAdHocDeclarations() {
        List<UserAdHocDeclareDTO> adHocDeclarations = adHocDeclareService.getCurrentUserAdHocDeclarations();
        return ResponseEntity.ok(adHocDeclarations);
    }

    /**
     * Get all ad hoc declarations for a specific user.
     *
     * @param userId the user ID
     * @return list of ad hoc declarations for the specified user
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserAdHocDeclareDTO>> getUserAdHocDeclarations(@PathVariable Integer userId) {
        List<UserAdHocDeclareDTO> adHocDeclarations = adHocDeclareService.getUserAdHocDeclarations(userId);
        return ResponseEntity.ok(adHocDeclarations);
    }

    /**
     * Get all ad hoc declarations with a specific status for the current user.
     *
     * @param status the declaration status
     * @return list of ad hoc declarations with the specified status
     */
    @GetMapping("/current-user/status/{status}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<List<UserAdHocDeclareDTO>> getCurrentUserAdHocDeclarationsByStatus(
            @PathVariable UserDeclarationStatus status) {
        List<UserAdHocDeclareDTO> adHocDeclarations = adHocDeclareService.getCurrentUserAdHocDeclarationsByStatus(status);
        return ResponseEntity.ok(adHocDeclarations);
    }

    /**
     * Create a new ad hoc declaration.
     *
     * @param requestDTO the request DTO containing the declaration data
     * @return the created ad hoc declaration
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<UserAdHocDeclareDTO> createAdHocDeclaration(
            @Valid @RequestBody UserAdHocDeclareRequestDTO requestDTO) {
        UserAdHocDeclareDTO createdDeclaration = adHocDeclareService.createAdHocDeclaration(requestDTO);
        return new ResponseEntity<>(createdDeclaration, HttpStatus.CREATED);
    }

    /**
     * Update the status of an ad hoc declaration.
     *
     * @param id the ad hoc declaration ID
     * @param status the new status
     * @return the updated ad hoc declaration
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<UserAdHocDeclareDTO> updateAdHocDeclarationStatus(
            @PathVariable Integer id,
            @Valid @RequestBody UserAdHocDeclareUpdateDTO updateDTO) {
        UserAdHocDeclareDTO updatedDeclaration = adHocDeclareService.updateAdHocDeclarationStatus(id, updateDTO);
        return ResponseEntity.ok(updatedDeclaration);
    }

    /**
     * Delete an ad hoc declaration.
     *
     * @param id the ad hoc declaration ID
     * @return response indicating success
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> deleteAdHocDeclaration(@PathVariable Integer id) {
        adHocDeclareService.deleteAdHocDeclaration(id);
        return ResponseEntity.ok(new ApiResponse("Ad hoc declaration deleted successfully"));
    }
}