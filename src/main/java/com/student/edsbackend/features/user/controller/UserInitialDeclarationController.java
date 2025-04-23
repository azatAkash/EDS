package com.student.edsbackend.features.user.controller;

import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationDTO;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationRequestDTO;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationStatusDTO;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationUpdateDTO;
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


    @PatchMapping("/{id}/status")
    @Operation(summary = "Send a user initial declaration for approval by ID",
            description = "Sets the declaration status to SENT_FOR_APPROVAL. Accessible by SUPER_ADMIN only")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    public ResponseEntity<UserInitialDeclarationDTO> changeStatusByUserId(@PathVariable Integer id, @RequestBody UserInitialDeclarationUpdateDTO updateDTO) {
        UserInitialDeclarationDTO updatedDeclaration = userInitialDeclarationService.changeStatusById(id, updateDTO);
        return ResponseEntity.ok(updatedDeclaration);
    }


    @PatchMapping("/{id}/responsible")
    @Operation(summary = "Verify a user initial declaration",
            description = "Changes the declaration status. Accessible by SUPER_ADMIN, ADMIN, and MANAGER")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<UserInitialDeclarationDTO> changeResponsible(
            @PathVariable Integer id,
            @RequestBody UserInitialDeclarationRequestDTO updateDTO) {
        UserInitialDeclarationDTO verifiedDeclaration = userInitialDeclarationService.updateResponsible(id, updateDTO);
        return ResponseEntity.ok(verifiedDeclaration);
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


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user initial declaration (soft delete)",
            description = "Marks the isDeleted field as true. Accessible by super_admin, admin, and manager")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteUserInitialDeclaration(@PathVariable Integer id) {
        userInitialDeclarationService.deleteUserInitialDeclaration(id);
        return ResponseEntity.noContent().build();
    }
}
