package com.student.edsbackend.features.declaration.initial.service;

import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOptionDTO;
import com.student.edsbackend.features.declaration.initial.dal.option.InitialDeclarationOptionRequestDTO;

import java.util.List;
import java.util.Optional;

public interface InitialDeclarationOptionService {

    /**
     * Get all non-deleted options
     */
    List<InitialDeclarationOptionDTO> getAllOptions();

    /**
     * Get all options for a specific question
     */
    List<InitialDeclarationOptionDTO> getOptionsByQuestionId(Integer questionId);

    /**
     * Get a specific option by ID if not deleted
     */
    Optional<InitialDeclarationOptionDTO> getOptionById(Integer id);

    /**
     * Create a new option
     */
    InitialDeclarationOptionDTO createOption(InitialDeclarationOptionRequestDTO request);

    void deleteOption(Integer id);
}
