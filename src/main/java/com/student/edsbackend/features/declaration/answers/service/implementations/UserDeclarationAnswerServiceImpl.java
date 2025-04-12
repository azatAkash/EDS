package com.student.edsbackend.features.declaration.answers.service.implementations;

import com.student.edsbackend.features.declaration.answers.UserDeclarationAdditionalAnswer;
import com.student.edsbackend.features.declaration.answers.UserDeclarationAdditionalAnswerRepository;
import com.student.edsbackend.features.declaration.answers.UserDeclarationAnswer;
import com.student.edsbackend.features.declaration.answers.UserDeclarationAnswerRepository;
import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationAnswerRequestDTO;
import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationAnswerResponseDTO;
import com.student.edsbackend.features.declaration.answers.service.UserDeclarationAnswerService;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationRepository;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOption;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOptionRepository;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOption;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOptionRepository;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserInitialDeclaration;
import com.student.edsbackend.features.user.dal.UserInitialDeclarationRepository;
import com.student.edsbackend.features.user.dal.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDeclarationAnswerServiceImpl implements UserDeclarationAnswerService {

    private final UserRepository userRepository;
    private final InitialDeclarationRepository initialDeclarationRepository;
    private final UserInitialDeclarationRepository userInitialDeclarationRepository;
    private final InitialDeclarationOptionRepository initialDeclarationOptionRepository;
    private final AdditionalAnswerOptionRepository additionalAnswerOptionRepository;
    private final UserDeclarationAnswerRepository userDeclarationAnswerRepository;
    private final UserDeclarationAdditionalAnswerRepository userDeclarationAdditionalAnswerRepository;

    @Override
    @Transactional
    public UserDeclarationAnswerResponseDTO saveUserDeclarationAnswers(UserDeclarationAnswerRequestDTO requestDTO) {
        // Get current user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        // Find the user by email
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Current user not found"));
        
        // Find the active declaration
        InitialDeclaration activeDeclaration = initialDeclarationRepository.findAll().stream()
                .filter(d -> d.getIsActive() && !d.getIsDeleted())
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "No active declaration found. Please ask Administrator to activate a declaration first."));
        
        // Find or create user's declaration
        UserInitialDeclaration userDeclaration = findUserDeclaration(currentUser, activeDeclaration);
        
        // Validate request
        if (requestDTO.getAnswers() == null || requestDTO.getAnswers().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No answers provided");
        }
        
        
        // Process each answer
        for (UserDeclarationAnswerRequestDTO.AnswerDTO answerDTO : requestDTO.getAnswers()) {
            // Validate option exists
            InitialDeclarationOption option = initialDeclarationOptionRepository.findById(answerDTO.getOptionId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Option not found with id: " + answerDTO.getOptionId()));
            
            // Create or update answer
            UserDeclarationAnswer answer = createAnswer(userDeclaration, option, answerDTO);
            
            
            // Process additional answers if present
            if (answerDTO.getAdditionalAnswers() != null && !answerDTO.getAdditionalAnswers().isEmpty()) {
                processAdditionalAnswers(answer, answerDTO.getAdditionalAnswers());
            }
        }
        
        // Build and return response
        return UserDeclarationAnswerResponseDTO.builder()
                .userDeclarationId(userDeclaration.getId())
                .userId(currentUser.getId())
                .userName(currentUser.getFirstname() + " " + currentUser.getLastname())
                .declarationId(activeDeclaration.getId())
                .creationDate(userDeclaration.getCreationDate())
                .status(userDeclaration.getStatus())
              
                .message("User declaration answers saved successfully")
                .build();
    }
    
    @Override
    public UserDeclarationAnswerResponseDTO getCurrentUserDeclarationAnswers() {
        // Get current user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        // Find the user by email
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Current user not found"));
        
        // Find the active declaration
        InitialDeclaration activeDeclaration = initialDeclarationRepository.findAll().stream()
                .filter(d -> d.getIsActive() && !d.getIsDeleted())
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "No active declaration found. Please ask Administrator to activate a declaration first."));
        
        // Find user's declaration
        Optional<UserInitialDeclaration> userDeclarationOpt = userInitialDeclarationRepository
                .findByUserIdAndDeclarationIdAndIsDeletedFalse(currentUser.getId(), activeDeclaration.getId());
        
        if (userDeclarationOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No declaration found for current user");
        }
        
        UserInitialDeclaration userDeclaration = userDeclarationOpt.get();
        
        // Count answers and additional answers
        List<UserDeclarationAnswer> answers = userDeclarationAnswerRepository.findAll().stream()
                .filter(a -> a.getUserDeclaration().getId().equals(userDeclaration.getId()) && !a.getIsDeleted())
                .toList();
        
        int additionalAnswersCount = 0;
        for (UserDeclarationAnswer answer : answers) {
            List<UserDeclarationAdditionalAnswer> additionalAnswers = 
                    userDeclarationAdditionalAnswerRepository.findByUserDeclarationAnswerIdAndIsDeletedFalse(answer.getId());
            additionalAnswersCount += additionalAnswers.size();
        }
        
        // Build and return response
        return UserDeclarationAnswerResponseDTO.builder()
                .userDeclarationId(userDeclaration.getId())
                .userId(currentUser.getId())
                .userName(currentUser.getFirstname() + " " + currentUser.getLastname())
                .declarationId(activeDeclaration.getId())
                .creationDate(userDeclaration.getCreationDate())
                .status(userDeclaration.getStatus())
                .additionalAnswersCount(additionalAnswersCount)
                .message("User declaration answers retrieved successfully")
                .build();
    }
    
    /**
     * Find existing user declaration or create a new one
     */
    private UserInitialDeclaration findUserDeclaration(User user, InitialDeclaration declaration) {
        Optional<UserInitialDeclaration> existingDeclaration = userInitialDeclarationRepository
                .findByUserIdAndDeclarationIdAndIsDeletedFalse(user.getId(), declaration.getId());
        
        if (existingDeclaration.isPresent()) {
            return existingDeclaration.get();
        }
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "No declaration found for current user"); 
    }
    
    /**
     * Create a new answer or update an existing one
     */
    private UserDeclarationAnswer createAnswer(UserInitialDeclaration userDeclaration, 
                                                    InitialDeclarationOption option,
                                                    UserDeclarationAnswerRequestDTO.AnswerDTO answerDTO) {
        // Check if answer already exists
        Optional<UserDeclarationAnswer> existingAnswer = userDeclarationAnswerRepository.findAll().stream()
                .filter(a -> a.getUserDeclaration().getId().equals(userDeclaration.getId()) && 
                       a.getOption().getId().equals(option.getId()) &&
                       !a.getIsDeleted())
                .findFirst();
        
        if (existingAnswer.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Answer already exists for this option");
        } else {
            // Create new answer
            UserDeclarationAnswer answer = UserDeclarationAnswer.builder()
                    .userDeclaration(userDeclaration)
                    .option(option)
                    .isAnswered(answerDTO.getIsAnswered())
                    .answer(answerDTO.getAnswer())
                    .isDeleted(false)
                    .build();
            return userDeclarationAnswerRepository.save(answer);
        }
    }
    
    /**
     * Process additional answers for a user declaration answer
     * 
     * @return The number of additional answers processed
     */
    private Void processAdditionalAnswers(UserDeclarationAnswer answer, 
                                        List<UserDeclarationAnswerRequestDTO.AdditionalAnswerGroupDTO> additionalAnswerGroups) {
        
        // Delete existing additional answers for this answer
        List<UserDeclarationAdditionalAnswer> existingAdditionalAnswers = 
                userDeclarationAdditionalAnswerRepository.findByUserDeclarationAnswerIdAndIsDeletedFalse(answer.getId());
        
        for (UserDeclarationAdditionalAnswer existingAnswer : existingAdditionalAnswers) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Additional answers already exist for this answer"); // TODO: Implement soft delete for additional answers and re-enable this erro
        }
        Short orderIndex = 1;
        // Process each group of additional answers
        for (UserDeclarationAnswerRequestDTO.AdditionalAnswerGroupDTO groupDTO : additionalAnswerGroups) {
            
            
            // Process each additional answer in the group
            for (UserDeclarationAnswerRequestDTO.AdditionalAnswerDTO additionalAnswerDTO : groupDTO.getAnswers()) {
                // Validate additional answer option exists
                AdditionalAnswerOption answerOption = additionalAnswerOptionRepository
                        .findById(additionalAnswerDTO.getAdditionalAnswerId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Additional answer option not found with id: " + additionalAnswerDTO.getAdditionalAnswerId()));
                
                // Create new additional answer
                UserDeclarationAdditionalAnswer additionalAnswer = UserDeclarationAdditionalAnswer.builder()
                        .answerOption(answerOption)
                        .userDeclarationAnswer(answer)
                        .answer(additionalAnswerDTO.getAnswer())
                        .orderIndex(orderIndex)
                        .isDeleted(false)
                        .build();
                
                userDeclarationAdditionalAnswerRepository.save(additionalAnswer);
                
            }
            orderIndex++;
        }
        return null;
    }
}