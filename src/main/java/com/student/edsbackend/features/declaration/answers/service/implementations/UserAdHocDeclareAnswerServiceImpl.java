package com.student.edsbackend.features.declaration.answers.service.implementations;

import com.student.edsbackend.features.declaration.adhoc.AdHocCategory;
import com.student.edsbackend.features.declaration.adhoc.UserAdHocDeclare;
import com.student.edsbackend.features.declaration.answers.UserAdHocDeclareAnswer;
import com.student.edsbackend.features.declaration.answers.UserAdHocDeclareAnswerRepository;
import com.student.edsbackend.features.declaration.answers.dto.UserAdHocDeclareAnswerDTO;
import com.student.edsbackend.features.declaration.answers.service.UserAdHocDeclareAnswerService;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the UserAdHocDeclareAnswerService interface
 */
@Service
@RequiredArgsConstructor
public class UserAdHocDeclareAnswerServiceImpl implements UserAdHocDeclareAnswerService {

    private final UserAdHocDeclareAnswerRepository userAdHocDeclareAnswerRepository;
    private final UserRepository userRepository;
    private final com.student.edsbackend.features.declaration.adhoc.repository.UserAdHocDeclareRepository userAdHocDeclareRepository;
    private final com.student.edsbackend.features.declaration.adhoc.repository.AdHocCategoryRepository adHocCategoryRepository;

    @Override
    @Transactional
    public UserAdHocDeclareAnswerDTO saveAnswer(UserAdHocDeclareAnswerDTO answerDTO) {
        // Get current user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Find the user by email
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));

        // Find the ad hoc declaration
        UserAdHocDeclare userAdHocDeclare = userAdHocDeclareRepository.findById(answerDTO.getUserAdHocDeclareId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad hoc declaration not found"));

        // Validate that the declaration belongs to the current user or user has appropriate permissions
        if (!userAdHocDeclare.getUser().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().name().equals("SUPER_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to save answers for this declaration");
        }
        if (userAdHocDeclare.getStatus()!= null && userAdHocDeclare.getStatus() != UserDeclarationStatus.CREATED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Declaration cannot be modified after it's been submitted or approved");
        }
        // Find the category if provided
        AdHocCategory category = null;
        if (answerDTO.getCategoryId() != null) {
            category = adHocCategoryRepository.findById(answerDTO.getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        }

        // Validate that either category or otherCategory is provided
        if (category == null && (answerDTO.getOtherCategory() == null || answerDTO.getOtherCategory().trim().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either category or otherCategory must be provided");
        }

        // Validate that conflictDescription is provided
        if (answerDTO.getConflictDescription() == null || answerDTO.getConflictDescription().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conflict description is required");
        }

        // Create and save the answer
        UserAdHocDeclareAnswer answer = UserAdHocDeclareAnswer.builder()
                .userAdHocDeclare(userAdHocDeclare)
                .category(category)
                .otherCategory(answerDTO.getOtherCategory())
                .conflictDescription(answerDTO.getConflictDescription())
                .build();

        answer = userAdHocDeclareAnswerRepository.save(answer);

        // Convert and return the saved answer as DTO
        return convertToDTO(answer);
    }

    @Override
    public UserAdHocDeclareAnswerDTO getAnswerById(Integer id) {
        UserAdHocDeclareAnswer answer = userAdHocDeclareAnswerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found"));

        // Check permissions
        checkPermission(answer.getUserAdHocDeclare().getUser().getId());

        return convertToDTO(answer);
    }

    @Override
    public List<UserAdHocDeclareAnswerDTO> getAnswersByUserAdHocDeclareId(Integer userAdHocDeclareId) {
        // Find the ad hoc declaration
        UserAdHocDeclare userAdHocDeclare = userAdHocDeclareRepository.findById(userAdHocDeclareId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ad hoc declaration not found"));

        // Check permissions
        checkPermission(userAdHocDeclare.getUser().getId());

        // Get and convert answers
        List<UserAdHocDeclareAnswer> answers = userAdHocDeclareAnswerRepository.findByUserAdHocDeclareId(userAdHocDeclareId);
        return answers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAnswer(Integer id) {
        UserAdHocDeclareAnswer answer = userAdHocDeclareAnswerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found"));

        // Check permissions
        checkPermission(answer.getUserAdHocDeclare().getUser().getId());

        // Delete the answer
        userAdHocDeclareAnswerRepository.delete(answer);
    }

    @Override
    public List<UserAdHocDeclareAnswerDTO> getCurrentUserAnswers() {
        // Get current user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Find the user by email
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));

        // Get and convert answers
        List<UserAdHocDeclareAnswer> answers = userAdHocDeclareAnswerRepository.findByUserId(currentUser.getId());
        return answers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert a UserAdHocDeclareAnswer entity to a DTO
     */
    private UserAdHocDeclareAnswerDTO convertToDTO(UserAdHocDeclareAnswer answer) {
        return UserAdHocDeclareAnswerDTO.builder()
                .id(answer.getId())
                .userAdHocDeclareId(answer.getUserAdHocDeclare().getId())
                .categoryId(answer.getCategory() != null ? answer.getCategory().getId() : null)
                .categoryDescription(answer.getCategory() != null ? answer.getCategory().getDescription() : null)
                .otherCategory(answer.getOtherCategory())
                .conflictDescription(answer.getConflictDescription())
                .build();
    }

    /**
     * Check if the current user has permission to access/modify the data
     */
    private void checkPermission(Integer ownerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));

        // Allow access if the user is the owner or has admin/manager role
        if (!currentUser.getId().equals(ownerId) && 
            !currentUser.getRole().name().equals("SUPER_ADMIN") && 
            !currentUser.getRole().name().equals("ADMIN") && 
            !currentUser.getRole().name().equals("MANAGER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to access this data");
        }
    }
}