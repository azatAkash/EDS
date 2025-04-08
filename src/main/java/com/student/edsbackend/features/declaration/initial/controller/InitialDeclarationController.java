package com.student.edsbackend.features.declaration.initial.controller;


import lombok.RequiredArgsConstructor;

import com.student.edsbackend.features.user.dal.User;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.declaration.initial.dal.InitialDeclaration;
import com.student.edsbackend.features.declaration.initial.dal.InitialDeclarationDTO;
import com.student.edsbackend.features.declaration.initial.service.InitialDeclarationService;
import com.student.edsbackend.features.user.dal.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder; 

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/declarations")
public class InitialDeclarationController {
    private final UserRepository userRepository;

    private final InitialDeclarationService declarationService;

    // Retrieve all declarations
    @GetMapping
    public ResponseEntity<List<InitialDeclarationDTO>> getAllDeclarations() {
        List<InitialDeclarationDTO> dtos = declarationService.getAllDeclarations();
        return ResponseEntity.ok(dtos);
    }
    @GetMapping("/{id}")
    public ResponseEntity<InitialDeclarationDTO> getDeclarationById(@PathVariable Integer id) {
        InitialDeclarationDTO declarationDTO = declarationService.getDeclarationById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Declaration not found with id: " + id));
        return ResponseEntity.ok(declarationDTO);
    }

    @PostMapping
    public ResponseEntity<InitialDeclarationDTO> createDeclaration(@RequestBody InitialDeclaration declaration) {
        // Get the currently authenticated user from the SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // The auth.getName() should return the username (e.g. the "sub" claim of the token)
        String userEmail = auth.getName();
        
        // Fetch the full User entity by email
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    
        // Set the createdBy field of the declaration to this user
        declaration.setCreatedBy(user);
    
        // Proceed with creating the declaration
        InitialDeclarationDTO createdDeclaration = declarationService.createDeclaration(declaration);
        return new ResponseEntity<>(createdDeclaration, HttpStatus.CREATED);
    }
    

    // // Update an existing declaration by its ID
    // @PutMapping("/{id}")
    // public ResponseEntity<InitialDeclaration> updateDeclaration(@PathVariable Integer id,
    //                                                               @RequestBody InitialDeclaration declaration) {
    //     InitialDeclaration updated = declarationService.updateDeclaration(id, declaration);
    //     return ResponseEntity.ok(updated);
    // }

    // Delete a declaration by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeclaration(@PathVariable Integer id) {
        declarationService.deleteDeclaration(id);
        return ResponseEntity.noContent().build();
    }
}
