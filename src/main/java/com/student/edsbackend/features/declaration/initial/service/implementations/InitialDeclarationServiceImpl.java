package com.student.edsbackend.features.declaration.initial.service.implementations;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationDTO;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationRepository;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationRequestDTO;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOption;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOptionRepository;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOption;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOptionRepository;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestion;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestionRepository;
import com.student.edsbackend.features.declaration.initial.service.InitialDeclarationService;
import com.student.edsbackend.features.user.dal.InitialDeclarationDetailedDTO;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InitialDeclarationServiceImpl implements InitialDeclarationService {

    private final InitialDeclarationRepository repository;
    private final UserRepository userRepository;
    private final InitialDeclarationQuestionRepository questionRepository;
    private final InitialDeclarationOptionRepository optionRepository;
    private final AdditionalAnswerOptionRepository additionalAnswerOptionRepository;

    @Override
    public List<InitialDeclarationDTO> getAllDeclarations() {
        return repository.findAll()
                .stream()
                .filter(declaration -> !declaration.getIsDeleted()) // Only include non-deleted declarations
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
    public Optional<InitialDeclarationDetailedDTO> getDeclarationById(Integer id) {
        return repository.findById(id)
                .filter(declaration -> !declaration.getIsDeleted()) // Only include non-deleted declarations
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

                    // Fetch questions for this declaration
                    List<InitialDeclarationDetailedDTO.QuestionDTO> questionDTOs = questionRepository.findByDeclarationIdAndIsDeletedFalseOrderByOrderNumberAsc(declaration.getId())
                            .stream()
                            .map(question -> {
                                // Fetch options for this question
                                List<InitialDeclarationDetailedDTO.OptionDTO> optionDTOs = optionRepository.findAll().stream()
                                        .filter(option -> option.getQuestion().getId().equals(question.getId()) && !option.getIsDeleted())
                                        .map(option -> {
                                            // Fetch additional options for this option
                                            List<InitialDeclarationDetailedDTO.AdditionalOptionDTO> additionalOptionDTOs = additionalAnswerOptionRepository.findAll().stream()
                                                    .filter(additionalOption -> additionalOption.getOption().getId().equals(option.getId()) && !additionalOption.getIsDeleted())
                                                    .map(additionalOption -> InitialDeclarationDetailedDTO.AdditionalOptionDTO.builder()
                                                            .id(additionalOption.getId())
                                                            .optionId(additionalOption.getOption().getId())
                                                            .description(additionalOption.getDescription())
                                                            .isRequired(additionalOption.getIsRequired())
                                                            .isDeleted(additionalOption.getIsDeleted())
                                                            .build())
                                                    .toList();

                                            return InitialDeclarationDetailedDTO.OptionDTO.builder()
                                                    .id(option.getId())
                                                    .questionId(option.getQuestion().getId())
                                                    .description(option.getDescription())
                                                    .additionalAnswerDescription(option.getAdditionalAnswerDescription())
                                                    .multipleAdditionalAnswers(option.getMultipleAdditionalAnswers())
                                                    .isConflict(option.getIsConflict())
                                                    .isDeleted(option.getIsDeleted())
                                                    .additionalOption(additionalOptionDTOs)
                                                    .build();
                                        })
                                        .toList();

                                return InitialDeclarationDetailedDTO.QuestionDTO.builder()
                                        .id(question.getId())
                                        .orderNumber(question.getOrderNumber().intValue())
                                        .declarationId(question.getDeclaration().getId())
                                        .description(question.getDescription())
                                        .questionType(question.getQuestionType().toString())
                                        .note(question.getNote())
                                        .isRequired(question.getIsRequired())
                                        .isDeleted(question.getIsDeleted())
                                        .options(optionDTOs)
                                        .build();
                            })
                            .toList();

                    return InitialDeclarationDetailedDTO.builder()
                            .id(declaration.getId())
                            .name(declaration.getName())
                            .creationDate(declaration.getCreationDate())
                            .activationDate(declaration.getActivationDate())
                            .isActive(declaration.getIsActive())
                            .isDeleted(declaration.getIsDeleted())
                            .createdBy(createdByDto)
                            .questions(questionDTOs)
                            .build();
                });
    }

    @Override
    @Transactional
    public InitialDeclarationDTO createDeclaration(InitialDeclarationRequestDTO requestDTO) {
        // Set a default name if none provided
        String name = requestDTO.getName();
        if (name == null || name.isEmpty()) {
            name = "New Initial Declaration";
        }

        // Configure activation flags and dates based on isActive
        Boolean isActive = requestDTO.getIsActive();
        LocalDateTime activationDate = LocalDateTime.now();
        if (isActive == null) {
            isActive = false;
            activationDate = null;
        } else if (isActive) {
            // If this declaration is being set as active, deactivate all other declarations
            repository.findAll().stream()
                    .filter(d -> d.getIsActive() && !d.getIsDeleted())
                    .forEach(d -> {
                        d.setIsActive(false);
                        d.setActivationDate(null);
                        repository.save(d);
                    });

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

    @Override
    public InitialDeclarationDTO activateDeclaration(Integer id) {
        return repository.findById(id)
                .filter(declaration -> !declaration.getIsDeleted()) // Only update non-deleted declarations
                .map(existing -> {
                
                    repository.findAll().stream()
                    .filter(d -> d.getIsActive() && !d.getIsDeleted())
                    .forEach(d -> {
                        d.setIsActive(false);
                        d.setActivationDate(null);
                        repository.save(d);
                    });

                    // Update the fields
                    existing.setIsActive(true);
                    existing.setActivationDate(LocalDateTime.now());
                    existing.setIsActive(true);
                    // Save the updated entity
                    InitialDeclaration savedDeclaration = repository.save(existing);

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

                    // Return the DTO with the updated entity data
                    return InitialDeclarationDTO.builder()
                            .id(savedDeclaration.getId())
                            .name(savedDeclaration.getName())
                            .creationDate(savedDeclaration.getCreationDate())
                            .activationDate(savedDeclaration.getActivationDate())
                            .isActive(savedDeclaration.getIsActive())
                            .isDeleted(savedDeclaration.getIsDeleted())
                            .createdBy(createdByDto)
                            .build();
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Declaration not found with id: " + id));
    }

    @Override
    public void deleteDeclaration(Integer id) {
        repository.findById(id)
                .filter(declaration -> !declaration.getIsDeleted()) // Only delete non-deleted declarations
                .ifPresentOrElse(declaration -> {
                    // Soft delete by setting isDeleted to true
                    declaration.setIsDeleted(true);
                    declaration.setIsActive(false);
                    repository.save(declaration);
                }, () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Declaration not found with id: " + id);
                });
    }
}
