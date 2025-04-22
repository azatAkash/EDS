package com.student.edsbackend.features.declaration.adhoc.service.implementations;

import com.student.edsbackend.features.declaration.adhoc.AdHocCategory;
import com.student.edsbackend.features.declaration.adhoc.UserAdHocDeclare;
import com.student.edsbackend.features.declaration.adhoc.UserAdHocExclude; // Assuming this import exists
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareAnswerRequestDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareAnswerResponseDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocDeclareRequestDTO;
import com.student.edsbackend.features.declaration.adhoc.dto.UserAdHocExcludeDTO; // Assuming this import exists
import com.student.edsbackend.features.declaration.adhoc.repository.UserAdHocDeclareRepository;
import com.student.edsbackend.features.declaration.adhoc.service.UserAdHocDeclareService;
import com.student.edsbackend.features.declaration.agreement.repository.DecAgreementStatementRepository;
import com.student.edsbackend.features.declaration.answers.UserAdHocDeclareAnswer;
import com.student.edsbackend.features.declaration.answers.UserAdHocDeclareAnswerRepository;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserRepository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.CollectionUtils; // For checking empty collections

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the UserAdHocDeclareService interface.
 * Handles business logic related to ad-hoc declarations.
 */
@Service
@RequiredArgsConstructor // Injects final fields via constructor
public class UserAdHocDeclareServiceImpl implements UserAdHocDeclareService {

    private static final Logger log = LoggerFactory.getLogger(UserAdHocDeclareServiceImpl.class);

    private final UserAdHocDeclareRepository userAdHocDeclareRepository;
    private final UserAdHocDeclareAnswerRepository userAdHocDeclareAnswerRepository;
    private final UserRepository userRepository;
    private final DecAgreementStatementRepository decAgreementStatementRepository;
    // --- Public Service Methods ---

    @Override
    @Transactional // Ensures atomicity
    public UserAdHocDeclareDTO createAdHocDeclaration(Integer userId, UserAdHocDeclareRequestDTO requestDTO) {
        log.info("Creating ad-hoc declaration for user ID: {}", userId);
        User user = findUserByIdOrThrow(userId);

        UserAdHocDeclare adHocDeclare = buildNewAdHocDeclare(user, requestDTO);
        UserAdHocDeclare savedDeclare = userAdHocDeclareRepository.save(adHocDeclare);

        // Process and save answers if provided
        if (CollectionUtils.isEmpty(requestDTO.getAnswers())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Answers cannot be empty");
        }

        List<UserAdHocDeclareAnswer> answers = processAndSaveAnswers(savedDeclare, requestDTO.getAnswers());
        savedDeclare.setAnswers(answers);

        log.info("Successfully created ad-hoc declaration with ID: {}", savedDeclare.getId());
        return convertToDTO(savedDeclare);
    }

    @Override
    @Transactional(readOnly = true) // Read-only transaction for query methods
    public Optional<UserAdHocDeclareDTO> getAdHocDeclarationById(Integer id) {
        log.debug("Fetching ad-hoc declaration by ID: {}", id);
        return userAdHocDeclareRepository.findByIdAndIsDeletedFalse(id)
                .map(this::convertToDTO); // Use method reference for conversion
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAdHocDeclareDTO> getAdHocDeclarationsByUserId(Integer userId) {
        log.debug("Fetching ad-hoc declarations for user ID: {}", userId);
        User user = findUserByIdOrThrow(userId);
        return userAdHocDeclareRepository.findByUserAndIsDeletedFalse(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAdHocDeclareDTO> getAdHocDeclarationsByStatus(UserDeclarationStatus status) {
        log.debug("Fetching ad-hoc declarations by status: {}", status);
        return userAdHocDeclareRepository.findByStatusAndIsDeletedFalse(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserAdHocDeclareDTO updateAdHocDeclarationStatus(Integer id, UserDeclarationStatus status,
            Integer responsibleUserId) {
        log.info("Updating status to {} for ad-hoc declaration ID: {} by responsible user ID: {}", status, id,
                responsibleUserId);
        UserAdHocDeclare adHocDeclare = findAdHocDeclareByIdOrThrow(id);
        User responsibleUser = findUserByIdOrThrow(responsibleUserId);

        adHocDeclare.setStatus(status);
        adHocDeclare.setResponsible(responsibleUser);
        // Note: Consider adding an 'updatedAt' timestamp field

        UserAdHocDeclare updatedDeclare = userAdHocDeclareRepository.save(adHocDeclare);
        log.info("Successfully updated status for ad-hoc declaration ID: {}", updatedDeclare.getId());
        return convertToDTO(updatedDeclare);
    }

    @Override
    @Transactional
    public void deleteAdHocDeclaration(Integer id) {
        log.warn("Performing soft delete for ad-hoc declaration ID: {}", id); // Use WARN for destructive actions
        UserAdHocDeclare adHocDeclare = findAdHocDeclareByIdOrThrow(id);

        adHocDeclare.setIsDeleted(true);
        // Note: Consider setting a 'deletedAt' timestamp and 'deletedBy' user
        userAdHocDeclareRepository.save(adHocDeclare);
        log.info("Successfully soft-deleted ad-hoc declaration ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAdHocDeclareDTO> getLatestAdHocDeclarationByUserId(Integer userId) {
        log.debug("Fetching latest ad-hoc declaration for user ID: {}", userId);
        // Assuming findLatestByUserId exists and correctly fetches the latest
        // non-deleted record
        return userAdHocDeclareRepository.findLatestByUserId(userId)
                .map(this::convertToDTO);
    }

    // --- Helper Methods ---

    /**
     * Finds a User by ID or throws a ResponseStatusException if not found.
     */
    private User findUserByIdOrThrow(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId);
                });
    }

    /**
     * Finds a non-deleted UserAdHocDeclare by ID or throws a
     * ResponseStatusException if not found.
     */
    private UserAdHocDeclare findAdHocDeclareByIdOrThrow(Integer declarationId) {
        return userAdHocDeclareRepository.findByIdAndIsDeletedFalse(declarationId)
                .orElseThrow(() -> {
                    log.error("Ad-hoc declaration not found or is deleted with ID: {}", declarationId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Ad hoc declaration not found with ID: " + declarationId);
                });
    }

    /**
     * Builds a new UserAdHocDeclare entity from request data.
     */
    private UserAdHocDeclare buildNewAdHocDeclare(User user, UserAdHocDeclareRequestDTO requestDTO) {

        List<Map<String, String>> agreedStatements = decAgreementStatementRepository.findAllDescriptionsAndIsDeletedFalse();

        if (agreedStatements.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No agreed statements found");
        }
        
        return UserAdHocDeclare.builder()
                .user(user)
                .createAt(LocalDateTime.now())
                .isDeleted(false)
                .status(UserDeclarationStatus.SENT_FOR_APPROVAL) // Default status on creation
                .hasAgreedWithStatements(requestDTO.getHasAgreedWithStatements())
                .agreedStatements(agreedStatements) // Map from DTO
                .createdBy(user) // The user submitting the declaration is the creator
                .responsible(null)
                .build();
    }

    /**
     * Processes and saves UserAdHocDeclareAnswer entities based on DTOs.
     */
    private List<UserAdHocDeclareAnswer> processAndSaveAnswers(UserAdHocDeclare declaration,
            List<UserAdHocDeclareAnswerRequestDTO> answerDTOs) {
        List<UserAdHocDeclareAnswer> answers = answerDTOs.stream()
                .map(dto -> buildAnswerEntity(declaration, dto))
                .collect(Collectors.toList());

        return userAdHocDeclareAnswerRepository.saveAll(answers); // Bulk save
    }

    /**
     * Builds a single UserAdHocDeclareAnswer entity.
     */
    private UserAdHocDeclareAnswer buildAnswerEntity(UserAdHocDeclare declaration,
            UserAdHocDeclareAnswerRequestDTO dto) {
        UserAdHocDeclareAnswer answer = new UserAdHocDeclareAnswer();
        answer.setUserAdHocDeclare(declaration); // Link to parent declaration

        if (dto.getCategoryId() != null) {
            // We only need the ID for the relationship mapping
            AdHocCategory category = new AdHocCategory();
            category.setId(dto.getCategoryId());
            answer.setCategory(category);
        }

        answer.setOtherCategory(dto.getOtherCategory());
        answer.setConflictDescription(dto.getConflictDescription());
        return answer;
    }

    /**
     * Converts a User entity to a UserDTO. Handles null input.
     */
    private UserDTO convertUserToDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .build();
    }

    /**
     * Converts a UserAdHocDeclareAnswer entity to a
     * UserAdHocDeclareAnswerResponseDTO.
     */
    private UserAdHocDeclareAnswerResponseDTO convertAnswerToDTO(UserAdHocDeclareAnswer answer) {
        if (answer == null) {
            return null;
        }

        // Retrieve the category description
        Map<String, String> categoryDescription = null;
        if (answer.getCategory() != null && answer.getCategory().getDescription() != null) {
            categoryDescription = answer.getCategory().getDescription();
        }

        return UserAdHocDeclareAnswerResponseDTO.builder()
                .categoryId(answer.getCategory() != null ? answer.getCategory().getId() : null)
                .otherCategory(answer.getOtherCategory())
                .conflictDescription(answer.getConflictDescription())
                .categoryDescription(categoryDescription) // Set the category description
                .build();
    }

    /**
     * Converts a UserAdHocExclude entity to a UserAdHocExcludeDTO. Handles null
     * input.
     * Assumes UserAdHocExcludeDTO has a suitable builder or constructor.
     */

    /**
     * Converts a UserAdHocDeclare entity to its DTO representation.
     */
    private UserAdHocDeclareDTO convertToDTO(UserAdHocDeclare entity) {
        if (entity == null) {
            return null;
        }

        // Convert related entities to DTOs using helper methods
        UserDTO userDTO = convertUserToDTO(entity.getUser());
        UserDTO responsibleDTO = convertUserToDTO(entity.getResponsible());
        UserDTO createdByDTO = convertUserToDTO(entity.getCreatedBy());

        // Convert answers
        List<UserAdHocDeclareAnswerResponseDTO> answerDTOs = CollectionUtils.isEmpty(entity.getAnswers())
                ? Collections.emptyList()
                : entity.getAnswers().stream()
                        .map(this::convertAnswerToDTO)
                        .collect(Collectors.toList());

        return UserAdHocDeclareDTO.builder()
                .id(entity.getId())
                .user(userDTO)
                .createAt(entity.getCreateAt())
                .isDeleted(entity.getIsDeleted())
                .responsible(responsibleDTO)
                .createdBy(createdByDTO)
                .status(entity.getStatus()) // Use the converted list
                .hasAgreedWithStatements(entity.getHasAgreedWithStatements())
                .statementAgreementStatuses(entity.getAgreedStatements()) // Map from entity field
                .answers(answerDTOs)
                .build();
    }
}