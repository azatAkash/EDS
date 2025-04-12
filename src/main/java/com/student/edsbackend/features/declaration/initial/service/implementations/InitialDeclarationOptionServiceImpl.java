package com.student.edsbackend.features.declaration.initial.service.implementations;

import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOption;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOptionDTO;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOptionRepository;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOptionRequestDTO;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestion;
import com.student.edsbackend.features.declaration.initial.dal.questions.InitialDeclarationQuestionRepository;
import com.student.edsbackend.features.declaration.initial.service.InitialDeclarationOptionService;
import com.student.edsbackend.features.enums.QuestionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InitialDeclarationOptionServiceImpl implements InitialDeclarationOptionService {

    private final InitialDeclarationOptionRepository optionRepository;
    private final InitialDeclarationQuestionRepository questionRepository;

    @Override
    public List<InitialDeclarationOptionDTO> getAllOptions() {
        return optionRepository.findAll().stream()
                .filter(option -> !option.getIsDeleted())
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InitialDeclarationOptionDTO> getOptionsByQuestionId(Integer questionId) {
        // Ensure the question exists
        questionRepository.findById(questionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Question not found with id: " + questionId));

        return optionRepository.findAll().stream()
                .filter(option -> !option.getIsDeleted()
                && option.getQuestion().getId().equals(questionId))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InitialDeclarationOptionDTO> getOptionById(Integer id) {
        return optionRepository.findById(id)
                .filter(option -> !option.getIsDeleted())
                .map(this::mapToDTO);
    }

    @Override
    public InitialDeclarationOptionDTO createOption(InitialDeclarationOptionRequestDTO request) {
        // Ensure the question exists

        InitialDeclarationQuestion question = ValidateYesNo(request);

        // Convert DTO to entity
        InitialDeclarationOption option = InitialDeclarationOption.builder()
                .question(question)
                .description(request.getDescription())
                .additionalAnswerDescription(request.getAdditionalAnswerDescription())
                .multipleAdditionalAnswers(request.getMultipleAdditionalAnswers())
                .isConflict(request.getIsConflict())
                .isDeleted(false) // Set default value
                .build();

        // Save the option
        InitialDeclarationOption savedOption = optionRepository.save(option);

        return mapToDTO(savedOption);
    }

    @Override
    public void deleteOption(Integer id) {
        // Find the option
        InitialDeclarationOption option = optionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Option not found with id: " + id));

        // Soft delete by setting isDeleted to true
        option.setIsDeleted(true);
        optionRepository.save(option);
    }

    /**
     * Maps an InitialDeclarationOption entity to its DTO representation
     */
    private InitialDeclarationOptionDTO mapToDTO(InitialDeclarationOption option) {
        return InitialDeclarationOptionDTO.builder()
                .id(option.getId())
                .questionId(option.getQuestion().getId())
                .description(option.getDescription())
                .additionalAnswerDescription(option.getAdditionalAnswerDescription())
                .multipleAdditionalAnswers(option.getMultipleAdditionalAnswers())
                .isConflict(option.getIsConflict())
                .isDeleted(option.getIsDeleted())
                .build();
    }

    private InitialDeclarationQuestion ValidateYesNo(InitialDeclarationOptionRequestDTO request) {
        InitialDeclarationQuestion question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Question not found with id: " + request.getQuestionId()));

        List<InitialDeclarationOption> existingOptions = optionRepository.findAll().stream()
                .filter(opt -> !opt.getIsDeleted()
                && opt.getQuestion().getId().equals(question.getId()))
                .collect(Collectors.toList());

        if (question.getQuestionType() == QuestionType.YES_NO) {

            // If we already have 2 options for this YES_NO question, don't allow more
            if (existingOptions.size() >= 2) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "YES_NO question type can only have two options");
            }

            if (existingOptions.size() == 1
                    && existingOptions.get(0).getDescription().equalsIgnoreCase(request.getDescription())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "YES_NO options must have different descriptions");
            }

            // If we have 2 options, make sure only one of them is marked as conflict
            if (existingOptions.size() == 2 && existingOptions.get(0).getIsConflict()
                    && existingOptions.get(1).getIsConflict()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Only one of the options can be marked as conflict");
            }
        } else if (question.getQuestionType() == QuestionType.AGREE) {
            if (!existingOptions.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Agree question type cannot have more than 1 option");
            }

            request.setIsConflict(false);

            request.setMultipleAdditionalAnswers(false);

        }

        return question;
    }
}
