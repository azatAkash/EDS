package com.student.edsbackend.features.user.controller;

import com.student.edsbackend.features.user.dal.UserInitialDeclarationDTO;
import com.student.edsbackend.features.user.dal.UserInitialDeclarationRequestDTO;
import com.student.edsbackend.features.user.dal.UserInitialDeclarationUpdateDTO;
import com.student.edsbackend.features.user.service.UserInitialDeclarationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/initial-declarations/answers")
@Tag(name = "User Initial Declarations", description = "Endpoints for managing user initial declarations")
public class UserInitialDeclarationController {

    private final UserInitialDeclarationService userInitialDeclarationService;

    @GetMapping("/{id}")
    @Operation(summary = "Get a user initial declaration by ID",
            description = "Accessible by super_admin, admin, manager, and user")
    public ResponseEntity<UserInitialDeclarationDTO> getUserInitialDeclarationById(@PathVariable Integer id) {
        UserInitialDeclarationDTO declarationDTO = userInitialDeclarationService.getUserInitialDeclarationById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User declaration not found with id: " + id));
        return ResponseEntity.ok(declarationDTO);
    }

    @GetMapping
    @Operation(summary = "Get all user initial declarations",
            description = "Accessible by super_admin, admin, and manager")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserInitialDeclarationDTO>> getAllUserInitialDeclarations() {
        List<UserInitialDeclarationDTO> declarations = userInitialDeclarationService.getAllUserInitialDeclarations();
        return ResponseEntity.ok(declarations);
    }

    @PostMapping
    @Operation(summary = "Create a new user initial declaration",
            description = "Accessible by super_admin, admin, and manager")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<UserInitialDeclarationDTO> createUserInitialDeclaration(
            @RequestBody UserInitialDeclarationRequestDTO requestDTO) {
        UserInitialDeclarationDTO createdDeclaration
                = userInitialDeclarationService.createUserInitialDeclaration(requestDTO);
        return new ResponseEntity<>(createdDeclaration, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user initial declaration",
            description = "Can update status, responsible, and isDeleted fields. Accessible by super_admin, admin, and manager")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<UserInitialDeclarationDTO> updateUserInitialDeclaration(
            @PathVariable Integer id,
            @RequestBody UserInitialDeclarationUpdateDTO updateDTO) {
        UserInitialDeclarationDTO updatedDeclaration
                = userInitialDeclarationService.updateUserInitialDeclaration(id, updateDTO);
        return ResponseEntity.ok(updatedDeclaration);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user initial declaration (soft delete)",
            description = "Marks the isDeleted field as true. Accessible by super_admin, admin, and manager")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteUserInitialDeclaration(@PathVariable Integer id) {
        userInitialDeclarationService.deleteUserInitialDeclaration(id);
        return ResponseEntity.noContent().build();
    }
}
