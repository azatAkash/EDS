package com.student.edsbackend.features.declaration.initial.service.implementations;

import com.student.edsbackend.configs.JsonConverter;
import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.declaration.answers.UserDeclarationAnswerRepository;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationDTO;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationRequestDTO;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationRepository;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOptionDTO;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestion;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestionDTO;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestionRepository;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestionRequestDTO;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestionUpdateDTO;
import com.student.edsbackend.features.declaration.initial.service.InitialDeclarationQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InitialDeclarationQuestionServiceImpl implements InitialDeclarationQuestionService {

    private final InitialDeclarationQuestionRepository questionRepository;
    private final InitialDeclarationRepository declarationRepository;
    private final UserDeclarationAnswerRepository userDeclarationAnswerRepository;

    @Override
    public List<InitialDeclarationQuestionDTO> getAllQuestions() {
        return questionRepository.findByIsDeletedFalse().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InitialDeclarationQuestionDTO> getQuestionsByDeclarationId(Integer declarationId) {
        return questionRepository.findByDeclarationIdAndIsDeletedFalse(declarationId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InitialDeclarationQuestionDTO> getQuestionById(Integer id) {
        return questionRepository.findById(id)
                .filter(q -> !q.getIsDeleted())
                .map(this::mapToDTO);
    }

    @Override
public InitialDeclarationQuestionDTO createQuestion(InitialDeclarationQuestionRequestDTO questionDTO) {
    InitialDeclaration declaration = declarationRepository.findById(questionDTO.getDeclarationId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Declaration not found with id: " + questionDTO.getDeclarationId()));

    Short finalOrderNumber = questionDTO.getOrderNumber();

    if (finalOrderNumber == null) {
        // Get max order from DB and add 1
        short maxOrder = questionRepository.findMaxOrderNumberByDeclarationId(declaration.getId());
        finalOrderNumber = (short) (maxOrder + 1);
    } else {
        // Check if the orderNumber is already taken
        boolean exists = questionRepository.existsByDeclarationIdAndOrderNumberAndIsDeletedFalse(declaration.getId(), finalOrderNumber);
        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Question order number already used for this declaration.");
        }
    }

    InitialDeclarationQuestion question = InitialDeclarationQuestion.builder()
            .orderNumber(finalOrderNumber)
            .declaration(declaration)
            .description(JsonConverter.ensureLangs(questionDTO.getDescription()))
            .questionType(questionDTO.getQuestionType())
            .note(JsonConverter.ensureLangs(questionDTO.getNote()))
            .isRequired(questionDTO.getIsRequired())
            .isDeleted(false)
            .build();

    return mapToDTO(questionRepository.save(question));
}


    @Override
    public InitialDeclarationQuestionDTO updateQuestion(Integer id, InitialDeclarationQuestionUpdateDTO questionDTO) {
        // Find the existing question and ensure it's not deleted
        InitialDeclarationQuestion existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Question not found with id: " + id));
        
        if (existingQuestion.getIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Cannot update a deleted question");
        }
        
        // Check if any user has answered this question
        if (userDeclarationAnswerRepository.existsAnswersForQuestion(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot update a question that has been answered by users");
        }
        
        // Update fields from DTO
        if (questionDTO.getOrderNumber() != null) {
            existingQuestion.setOrderNumber(questionDTO.getOrderNumber());
        }
        if (questionDTO.getDescription() != null) {
            existingQuestion.setDescription(questionDTO.getDescription());
        }
        if (questionDTO.getQuestionType() != null) {
            existingQuestion.setQuestionType(questionDTO.getQuestionType());
        }
        if (questionDTO.getNote() != null) {
            existingQuestion.setNote(questionDTO.getNote());
        }
        if (questionDTO.getIsRequired() != null) {
            existingQuestion.setIsRequired(questionDTO.getIsRequired());
        }

        
        // Save the updated question
        InitialDeclarationQuestion updatedQuestion = questionRepository.save(existingQuestion);
        
        return mapToDTO(updatedQuestion);
    }

    @Override
    public void deleteQuestion(Integer id) {
        // Find the question
        InitialDeclarationQuestion question = questionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Question not found with id: " + id));
        
        // Soft delete by setting isDeleted to true
        question.setIsDeleted(true);
        questionRepository.save(question);
    }
    
    /**
     * Maps an InitialDeclarationQuestion entity to its DTO representation
     */
    private InitialDeclarationQuestionDTO mapToDTO(InitialDeclarationQuestion question) {
        
        return InitialDeclarationQuestionDTO.builder()
                .id(question.getId())
                .orderNumber(question.getOrderNumber())
                .declarationId(question.getDeclaration().getId())
                .description(JsonConverter.ensureLangs(question.getDescription()))
                .questionType(question.getQuestionType())
                .note(JsonConverter.ensureLangs(question.getNote()))
                .isRequired(question.getIsRequired())
                .isDeleted(question.getIsDeleted())
                .build();
    }
}