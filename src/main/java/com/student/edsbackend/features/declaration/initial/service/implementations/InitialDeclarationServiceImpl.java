package com.student.edsbackend.features.declaration.initial.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationDTO;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationRepository;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationRequestDTO;
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
    public InitialDeclarationDTO createDeclaration(InitialDeclarationRequestDTO requestDTO) {
        // Set a default name if none provided
        String name = requestDTO.getName();
        if (name == null || name.isEmpty()) {
            name = "New Initial Declaration";
        }

        
        // Configure activation flags and dates based on isActive
        Boolean isActive = requestDTO.getIsActive();
        LocalDateTime activationDate = requestDTO.getActivationDate();
        if (isActive == null) {
            isActive = false;
            activationDate = null;
        } else if (isActive) {
            activationDate = LocalDateTime.now();
        }

        // Retrieve the current authenticated user's email from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Lookup the full User entity using UserRepository
        User createdBy = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        // Create the entity from the request DTO
        InitialDeclaration declaration = InitialDeclaration.builder()
                .name(name)
                .creationDate(LocalDateTime.now())
                .activationDate(activationDate)
                .isActive(isActive)
                .isDeleted(false) // Default value for new declarations
                .createdBy(createdBy)
                .build();

        // Save the entity
        InitialDeclaration savedDeclaration = repository.save(declaration);

        // Create UserDTO for the response
        UserDTO createdByDto = UserDTO.builder()
                .id(savedDeclaration.getCreatedBy().getId())
                .firstname(savedDeclaration.getCreatedBy().getFirstname())
                .lastname(savedDeclaration.getCreatedBy().getLastname())
                .email(savedDeclaration.getCreatedBy().getEmail())
                .middlename(savedDeclaration.getCreatedBy().getMiddlename())
                .role(savedDeclaration.getCreatedBy().getRole())
                .position(savedDeclaration.getCreatedBy().getPosition())
                .department(savedDeclaration.getCreatedBy().getDepartment())
                .isActive(savedDeclaration.getCreatedBy().getIsActive())
                .isDeleted(savedDeclaration.getCreatedBy().getIsDeleted())
                .registrationDate(savedDeclaration.getCreatedBy().getRegistrationDate())
                .build();

        // Return the DTO with the saved entity data
        return InitialDeclarationDTO.builder()
                .id(savedDeclaration.getId())
                .name(savedDeclaration.getName())
                .creationDate(savedDeclaration.getCreationDate())
                .activationDate(savedDeclaration.getActivationDate())
                .isActive(savedDeclaration.getIsActive())
                .isDeleted(savedDeclaration.getIsDeleted())
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
