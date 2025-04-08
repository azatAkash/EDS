package com.student.edsbackend.features.declaration.initial.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.student.edsbackend.features.declaration.initial.dal.InitialDeclaration;
import com.student.edsbackend.features.declaration.initial.dal.InitialDeclarationDTO;
import com.student.edsbackend.features.declaration.initial.dal.InitialDeclarationRepository;
import com.student.edsbackend.features.declaration.initial.service.InitialDeclarationService;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InitialDeclarationServiceImpl implements InitialDeclarationService {

    private final InitialDeclarationRepository repository;
    private final UserRepository userRepository;

    @Override
    public List<InitialDeclarationDTO> getAllDeclarations() {
        return repository.findAll()
                .stream()
                .map(declaration -> {
                    // Build a simple UserDTO for the createdBy user to avoid recursive references.
                    UserDTO createdByDto = UserDTO.builder()
                            .id(declaration.getCreatedBy().getId())
                            .firstname(declaration.getCreatedBy().getFirstname())
                            .lastname(declaration.getCreatedBy().getLastname())
                            .email(declaration.getCreatedBy().getEmail())
                            .middlename(declaration.getCreatedBy().getMiddlename())
                            .role(declaration.getCreatedBy().getRole())
                            .position(declaration.getCreatedBy().getPosition())
                            .department(declaration.getCreatedBy().getDepartment())
                            .isActive(declaration.getCreatedBy().getIsActive())
                            .isDeleted(declaration.getCreatedBy().getIsDeleted())
                            .registrationDate(declaration.getCreatedBy().getRegistrationDate())
                            .build();

                    return InitialDeclarationDTO.builder()
                            .id(declaration.getId())
                            .name(declaration.getName())
                            .creationDate(declaration.getCreationDate())
                            .activationDate(declaration.getActivationDate())
                            .isActive(declaration.getIsActive())
                            .isDeleted(declaration.getIsDeleted())
                            .createdBy(createdByDto)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InitialDeclarationDTO> getDeclarationById(Integer id) {
        return repository.findById(id)
                .map(declaration -> {
                    // Build a simple UserDTO for the createdBy relationship
                    UserDTO createdByDto = UserDTO.builder()
                            .id(declaration.getCreatedBy().getId())
                            .firstname(declaration.getCreatedBy().getFirstname())
                            .lastname(declaration.getCreatedBy().getLastname())
                            .email(declaration.getCreatedBy().getEmail())
                            .middlename(declaration.getCreatedBy().getMiddlename())
                            .role(declaration.getCreatedBy().getRole())
                            .position(declaration.getCreatedBy().getPosition())
                            .department(declaration.getCreatedBy().getDepartment())
                            .isActive(declaration.getCreatedBy().getIsActive())
                            .isDeleted(declaration.getCreatedBy().getIsDeleted())
                            .registrationDate(declaration.getCreatedBy().getRegistrationDate())
                            .build();

                    return InitialDeclarationDTO.builder()
                            .id(declaration.getId())
                            .name(declaration.getName())
                            .creationDate(declaration.getCreationDate())
                            .activationDate(declaration.getActivationDate())
                            .isActive(declaration.getIsActive())
                            .isDeleted(declaration.getIsDeleted())
                            .createdBy(createdByDto)
                            .build();
                });
    }

    @Override
    public InitialDeclarationDTO createDeclaration(InitialDeclaration declaration) {
        // Set a default name if none provided
        if (declaration.getName() == null || declaration.getName().isEmpty()) {
            declaration.setName("New Initial Declaration");
        }

        // Set the creation date to now
        declaration.setCreationDate(LocalDateTime.now());

        // Configure activation flags and dates based on isActive
        if (declaration.getIsActive() == null) {
            declaration.setIsActive(false);
            declaration.setActivationDate(null);
        } else if (declaration.getIsActive()) {
            declaration.setActivationDate(LocalDateTime.now());
        }

        // Retrieve the current authenticated user's email from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Lookup the full User entity using UserRepository.
        // (Assumes userRepository is injected in this service.)
        User createdBy = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Set the createdBy field from the authenticated user
        declaration.setCreatedBy(createdBy);

        repository.save(declaration);

        UserDTO createdByDto = UserDTO.builder()
                .id(declaration.getCreatedBy().getId())
                .firstname(declaration.getCreatedBy().getFirstname())
                .lastname(declaration.getCreatedBy().getLastname())
                .email(declaration.getCreatedBy().getEmail())
                .middlename(declaration.getCreatedBy().getMiddlename())
                .role(declaration.getCreatedBy().getRole())
                .position(declaration.getCreatedBy().getPosition())
                .department(declaration.getCreatedBy().getDepartment())
                .isActive(declaration.getCreatedBy().getIsActive())
                .isDeleted(declaration.getCreatedBy().getIsDeleted())
                .registrationDate(declaration.getCreatedBy().getRegistrationDate())
                .build();

        return InitialDeclarationDTO.builder()
                .id(declaration.getId())
                .name(declaration.getName())
                .creationDate(declaration.getCreationDate())
                .activationDate(declaration.getActivationDate())
                .isActive(declaration.getIsActive())
                .isDeleted(declaration.getIsDeleted())
                .createdBy(createdByDto)
                .build();
    }

    // @Override
    // public InitialDeclaration updateDeclaration(Integer id, InitialDeclaration
    // declaration) {
    // return repository.findById(id)
    // .map(existing -> {
    // existing.setName(declaration.getName());
    // existing.setActivationDate(declaration.getActivationDate());
    // existing.setIsActive(declaration.getIsActive());
    // existing.setIsDeleted(declaration.getIsDeleted());
    // // Optionally update relationships if required
    // return repository.save(existing);
    // })
    // .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
    // "Declaration not found with id: " + id));
    // }

    @Override
    public void deleteDeclaration(Integer id) {
        repository.findById(id)
                .ifPresentOrElse(repository::delete,
                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Declaration not found with id: " + id);
                        });
    }
}
