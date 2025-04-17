package com.student.edsbackend.features.management.controller;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.management.dto.UserManagementPlanDTO;
import com.student.edsbackend.features.management.dto.UserManagementPlanRequestDTO;
import com.student.edsbackend.features.management.service.UserManagementPlanService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for managing user management plans
 * Endpoints are accessible to super admin, admin, managers, and users assigned to the management plan
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/management-plans/")
public class UserManagementPlanController {

    private final UserManagementPlanService managementPlanService;

    /**
     * Get a management plan by ID
     * Accessible to super admin, admin, managers, and users assigned to the management plan
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserManagementPlanDTO> getManagementPlanById(@PathVariable Integer id) {
        UserManagementPlanDTO managementPlan = managementPlanService.getManagementPlanById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Management plan with id " + id + " not found"));
        return ResponseEntity.ok(managementPlan);
    }

    /**
     * Get all management plans
     * For super admin, admin, and managers: returns all management plans
     * For regular users: returns only their management plans
     */
    @GetMapping
    public ResponseEntity<List<UserManagementPlanDTO>> getAllManagementPlans() {
        List<UserManagementPlanDTO> managementPlans = managementPlanService.getAllManagementPlans();
        return ResponseEntity.ok(managementPlans);
    }

    /**
     * Get management plans for the current user
     * Accessible to all authenticated users
     */
    @GetMapping("/my-plans")
    public ResponseEntity<List<UserManagementPlanDTO>> getCurrentUserManagementPlans() {
        List<UserManagementPlanDTO> managementPlans = managementPlanService.getCurrentUserManagementPlans();
        return ResponseEntity.ok(managementPlans);
    }

    /**
     * Create a new management plan
     * Accessible to super admin, admin, and managers
     * Requires either userDeclarationId or adHocId to be provided
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<UserManagementPlanDTO> createManagementPlan(
            @Valid @RequestBody UserManagementPlanRequestDTO requestDTO) {
        UserManagementPlanDTO createdPlan = managementPlanService.createManagementPlan(requestDTO);
        return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
    }

    /**
     * Delete a management plan by ID
     * Accessible to super admin, admin, and the creator of the management plan
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<?> deleteManagementPlan(@PathVariable Integer id) {
        boolean deleted = managementPlanService.deleteManagementPlan(id);
        if (!deleted) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Management plan with id " + id + " not found");
        }
        return ResponseEntity.ok(new ApiResponse("Management plan deleted successfully"));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<?> ammnedManagementPlan(@PathVariable Integer id) {
        boolean deleted = managementPlanService.ammendManagementPlan(id);
        if (!deleted) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Management plan with id " + id + " not found");
        }
        return ResponseEntity.ok(new ApiResponse("Management plan ammended successfully"));
    }
}