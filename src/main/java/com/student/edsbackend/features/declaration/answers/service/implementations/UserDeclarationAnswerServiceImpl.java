package com.student.edsbackend.features.declaration.answers.service.implementations;

import com.student.edsbackend.features.declaration.answers.UserDeclarationAdditionalAnswer;
import com.student.edsbackend.features.declaration.answers.UserDeclarationAdditionalAnswerRepository;
import com.student.edsbackend.features.declaration.answers.UserDeclarationAnswer;
import com.student.edsbackend.features.declaration.answers.UserDeclarationAnswerRepository;
import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationAnswerRequestDTO;
import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationAnswerResponseDTO;
import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationDetailedResponseDTO;
import com.student.edsbackend.features.declaration.answers.service.UserDeclarationAnswerService;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationRepository;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOption;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOptionRepository;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOption;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOptionRepository;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserRepository;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclaration;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        
        // List to store processed answers
        List<UserDeclarationAnswerResponseDTO.AnswerDTO> processedAnswers = new ArrayList<>();
        
        // Process each answer
        for (UserDeclarationAnswerRequestDTO.AnswerDTO answerDTO : requestDTO.getAnswers()) {
            // Validate option exists
            InitialDeclarationOption option = initialDeclarationOptionRepository.findById(answerDTO.getOptionId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Option not found with id: " + answerDTO.getOptionId()));
            
            // Create or update answer
            UserDeclarationAnswer answer = createAnswer(userDeclaration, option, answerDTO);
            
            // Create a copy of the answer DTO to add to processed answers
            UserDeclarationAnswerResponseDTO.AnswerDTO processedAnswerDTO = UserDeclarationAnswerResponseDTO.AnswerDTO.builder()
                    .optionId(answer.getOption().getId())
                    .isAnswered(answer.getIsAnswered())
                    .answer(answer.getAnswer())
                    .build();
            
            // Process additional answers if present
            if (answerDTO.getAdditionalAnswers() != null && !answerDTO.getAdditionalAnswers().isEmpty()) {
                processAdditionalAnswers(answer, answerDTO.getAdditionalAnswers());
                
                // Get saved additional answers for this answer
                List<UserDeclarationAdditionalAnswer> savedAdditionalAnswers = 
                        userDeclarationAdditionalAnswerRepository.findByUserDeclarationAnswerIdAndIsDeletedFalse(answer.getId());
                
                // Group additional answers by order index
                List<UserDeclarationAnswerResponseDTO.AdditionalAnswerGroupDTO> processedAdditionalAnswerGroups = new ArrayList<>();
                
                // Create a map to group additional answers by order index
                Map<Short, List<UserDeclarationAdditionalAnswer>> answersByOrderIndex = savedAdditionalAnswers.stream()
                        .collect(Collectors.groupingBy(UserDeclarationAdditionalAnswer::getOrderIndex));
                
                // Process each group
                for (Map.Entry<Short, List<UserDeclarationAdditionalAnswer>> entry : answersByOrderIndex.entrySet()) {
                    List<UserDeclarationAnswerResponseDTO.AdditionalAnswerDTO> additionalAnswerDTOs = new ArrayList<>();
                    
                    // Convert each additional answer to DTO
                    for (UserDeclarationAdditionalAnswer additionalAnswer : entry.getValue()) {
                        UserDeclarationAnswerResponseDTO.AdditionalAnswerDTO additionalAnswerDTO = 
                                UserDeclarationAnswerResponseDTO.AdditionalAnswerDTO.builder()
                                        .additionalAnswerId(additionalAnswer.getAnswerOption().getId())
                                        .answer(additionalAnswer.getAnswer())
                                        .build();
                        additionalAnswerDTOs.add(additionalAnswerDTO);
                    }
                    
                    // Create group DTO
                    UserDeclarationAnswerResponseDTO.AdditionalAnswerGroupDTO groupDTO = 
                            UserDeclarationAnswerResponseDTO.AdditionalAnswerGroupDTO.builder()
                                    .answers(additionalAnswerDTOs)
                                    .build();
                    
                    processedAdditionalAnswerGroups.add(groupDTO);
                }
                
                // Set additional answers to processed answer DTO
                processedAnswerDTO.setAdditionalAnswers(processedAdditionalAnswerGroups);
            }
            
            processedAnswers.add(processedAnswerDTO);
        }
        
        // Build and return response with processed answers
        return UserDeclarationAnswerResponseDTO.builder()
                .userDeclarationId(userDeclaration.getId())
                .userId(currentUser.getId())
                .userName(currentUser.getFirstname() + " " + currentUser.getLastname())
                .declarationId(activeDeclaration.getId())
                .creationDate(userDeclaration.getCreationDate())
                .status(userDeclaration.getStatus())
                .answers(processedAnswers)
                .message("User declaration answers saved successfully")
                .build();
    }
    
    @Override
    public UserDeclarationDetailedResponseDTO getCurrentUserDeclarationAnswers() {
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
        
        // Get all declaration questions and options
        List<InitialDeclarationOption> allOptions = initialDeclarationOptionRepository.findAll().stream()
                .filter(o -> !o.getIsDeleted())
                .toList();
                
        // Get all user answers for this declaration
        List<UserDeclarationAnswer> userAnswers = userDeclarationAnswerRepository.findAll().stream()
                .filter(a -> a.getUserDeclaration().getId().equals(userDeclaration.getId()) && !a.getIsDeleted())
                .toList();
                
        // Create a map of optionId -> UserDeclarationAnswer for quick lookup
        Map<Integer, UserDeclarationAnswer> optionAnswerMap = userAnswers.stream()
                .collect(Collectors.toMap(
                    answer -> answer.getOption().getId(),
                    answer -> answer
                ));
                
        // Group options by question
        Map<Integer, List<InitialDeclarationOption>> optionsByQuestionId = allOptions.stream()
                .collect(Collectors.groupingBy(option -> option.getQuestion().getId()));
                
        // Get all questions for this declaration
        List<UserDeclarationDetailedResponseDTO.QuestionWithAnswerDTO> questionsWithAnswers = new ArrayList<>();
        
        // For each question, build the question with options and answers
        activeDeclaration.getQuestions().stream()
                .filter(q -> !q.getIsDeleted())
                .forEach(question -> {
                    List<UserDeclarationDetailedResponseDTO.OptionWithAnswerDTO> optionsWithAnswers = new ArrayList<>();
                    
                    // Get options for this question
                    List<InitialDeclarationOption> questionOptions = optionsByQuestionId.getOrDefault(question.getId(), new ArrayList<>());
                    
                    // For each option, build the option with answer
                    for (InitialDeclarationOption option : questionOptions) {
                        UserDeclarationAnswer userAnswer = optionAnswerMap.get(option.getId());
                        
                        // Build option with answer
                        UserDeclarationDetailedResponseDTO.OptionWithAnswerDTO optionWithAnswer = 
                                UserDeclarationDetailedResponseDTO.OptionWithAnswerDTO.builder()
                                .id(option.getId())
                                .description(option.getDescription())
                                .additionalAnswerDescription(option.getAdditionalAnswerDescription())
                                .multipleAdditionalAnswers(option.getMultipleAdditionalAnswers())
                                .isConflict(option.getIsConflict())
                                .isAnswered(userAnswer != null ? userAnswer.getIsAnswered() : false)
                                .answer(userAnswer != null ? userAnswer.getAnswer() : null)
                                .build();
                        
                        // If user has answered this option, get additional answers
                        if (userAnswer != null) {
                            List<UserDeclarationAdditionalAnswer> additionalAnswers = 
                                    userDeclarationAdditionalAnswerRepository.findByUserDeclarationAnswerIdAndIsDeletedFalse(userAnswer.getId());
                            
                            if (!additionalAnswers.isEmpty()) {
                                // Group additional answers by order index
                                Map<Short, List<UserDeclarationAdditionalAnswer>> answersByOrderIndex = additionalAnswers.stream()
                                        .collect(Collectors.groupingBy(UserDeclarationAdditionalAnswer::getOrderIndex));
                                
                                List<UserDeclarationDetailedResponseDTO.AdditionalAnswerGroupDTO> additionalAnswerGroups = new ArrayList<>();
                                
                                // Process each group
                                for (Map.Entry<Short, List<UserDeclarationAdditionalAnswer>> entry : answersByOrderIndex.entrySet()) {
                                    List<UserDeclarationDetailedResponseDTO.AdditionalAnswerDTO> additionalAnswerDTOs = new ArrayList<>();
                                    
                                    // Convert each additional answer to DTO
                                    for (UserDeclarationAdditionalAnswer additionalAnswer : entry.getValue()) {
                                        AdditionalAnswerOption answerOption = additionalAnswer.getAnswerOption();
                                        
                                        UserDeclarationDetailedResponseDTO.AdditionalAnswerDTO additionalAnswerDTO = 
                                                UserDeclarationDetailedResponseDTO.AdditionalAnswerDTO.builder()
                                                        .additionalAnswerId(answerOption.getId())
                                                        .description(answerOption.getDescription())
                                                        .isRequired(answerOption.getIsRequired())
                                                        .answer(additionalAnswer.getAnswer())
                                                        .build();
                                        additionalAnswerDTOs.add(additionalAnswerDTO);
                                    }
                                    
                                    // Create group DTO
                                    UserDeclarationDetailedResponseDTO.AdditionalAnswerGroupDTO groupDTO = 
                                            UserDeclarationDetailedResponseDTO.AdditionalAnswerGroupDTO.builder()
                                                    .answers(additionalAnswerDTOs)
                                                    .build();
                                    
                                    additionalAnswerGroups.add(groupDTO);
                                }
                                
                                // Set additional answers to option
                                optionWithAnswer.setAdditionalAnswers(additionalAnswerGroups);
                            }
                        }
                        
                        optionsWithAnswers.add(optionWithAnswer);
                    }
                    
                    // Build question with options and answers
                    UserDeclarationDetailedResponseDTO.QuestionWithAnswerDTO questionWithAnswer = 
                            UserDeclarationDetailedResponseDTO.QuestionWithAnswerDTO.builder()
                            .id(question.getId())
                            .description(question.getDescription())
                            .questionType(question.getQuestionType().toString())
                            .note(question.getNote())
                            .isRequired(question.getIsRequired())
                            .optionsWithAnswers(optionsWithAnswers)
                            .build();
                    
                    questionsWithAnswers.add(questionWithAnswer);
                });
        
        // Build and return response
        return UserDeclarationDetailedResponseDTO.builder()
                .userDeclarationId(userDeclaration.getId())
                .userId(currentUser.getId())
                .userName(currentUser.getFirstname() + " " + currentUser.getLastname())
                .declarationId(activeDeclaration.getId())
                .declarationName(activeDeclaration.getName())
                .creationDate(userDeclaration.getCreationDate())
                .status(userDeclaration.getStatus())
                .questionsWithAnswers(questionsWithAnswers)
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