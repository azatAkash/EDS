package com.student.edsbackend.features.declaration.initial.service.implementations;

import com.student.edsbackend.configs.JsonConverter;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOption;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOptionDTO;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOptionRepository;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOptionRequestDTO;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOptionUpdateDTO;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOption;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOptionRepository;
import com.student.edsbackend.features.declaration.initial.service.AdditionalAnswerOptionService;
import com.student.edsbackend.features.enums.QuestionType;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdditionalAnswerOptionServiceImpl implements AdditionalAnswerOptionService {

    private final AdditionalAnswerOptionRepository additionalAnswerOptionRepository;
    private final InitialDeclarationOptionRepository optionRepository;
    private final UserInitialDeclarationRepository userInitialDeclarationRepository;

    @Override
    public Optional<AdditionalAnswerOptionDTO> getAdditionalAnswerOptionById(Integer id) {
        return additionalAnswerOptionRepository.findById(id)
                .filter(option -> !option.getIsDeleted())
                .map(this::mapToDTO);
    }

    @Override
    public AdditionalAnswerOptionDTO createAdditionalAnswerOption(AdditionalAnswerOptionRequestDTO request) {
        // Ensure the parent option exists
        InitialDeclarationOption option = optionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Option not found with id: " + request.getOptionId()));
        
               
                if (option.getQuestion().getDeclaration().getIsActive()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Declaration is active");
                }
        
                if (!userInitialDeclarationRepository.findAllByDeclarationId(option.getQuestion().getDeclaration().getId()).isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Declaration already answers");
                }

        // Check if the option's question type is YES_NO
        if (option.getQuestion().getQuestionType() != QuestionType.YES_NO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Additional answer options can only be created for YES_NO question types");
        }

        // Convert DTO to entity
        AdditionalAnswerOption additionalAnswerOption = AdditionalAnswerOption.builder()
                .option(option)
                .description(JsonConverter.ensureLangsStrict(request.getDescription()))
                .isRequired(request.getIsRequired())
                .isDeleted(false) // Set default value
                .build();

        // Save the additional answer option
        AdditionalAnswerOption savedOption = additionalAnswerOptionRepository.save(additionalAnswerOption);

        return mapToDTO(savedOption);
    }

    @Override
    public AdditionalAnswerOptionDTO updateAdditionalAnswerOption(Integer id, AdditionalAnswerOptionUpdateDTO request) {
        // Find the additional answer option
        AdditionalAnswerOption additionalAnswerOption = additionalAnswerOptionRepository.findById(id)
                .filter(option -> !option.getIsDeleted())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Additional answer option not found with id: " + id));

        // Update fields - optionId is not allowed to be changed
        additionalAnswerOption.setDescription(JsonConverter.ensureLangsStrict(request.getDescription()));
        additionalAnswerOption.setIsRequired(request.getIsRequired());

        // Save the updated additional answer option
        AdditionalAnswerOption updatedOption = additionalAnswerOptionRepository.save(additionalAnswerOption);

        return mapToDTO(updatedOption);
    }

    @Override
    public void deleteAdditionalAnswerOption(Integer id) {
        // Find the additional answer option
        AdditionalAnswerOption additionalAnswerOption = additionalAnswerOptionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Additional answer option not found with id: " + id));

        // Soft delete by setting isDeleted to true
        additionalAnswerOption.setIsDeleted(true);
        additionalAnswerOptionRepository.save(additionalAnswerOption);
    }

    /**
     * Maps an AdditionalAnswerOption entity to its DTO representation
     */
    private AdditionalAnswerOptionDTO mapToDTO(AdditionalAnswerOption additionalAnswerOption) {

        return AdditionalAnswerOptionDTO.builder()
                .id(additionalAnswerOption.getId())
                .optionId(additionalAnswerOption.getOption().getId())
                .description(additionalAnswerOption.getDescription())
                .isRequired(additionalAnswerOption.getIsRequired())
                .isDeleted(additionalAnswerOption.getIsDeleted())
                .build();
    }
}
