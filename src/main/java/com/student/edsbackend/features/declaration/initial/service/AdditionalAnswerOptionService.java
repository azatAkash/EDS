package com.student.edsbackend.features.declaration.initial.service;

import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOptionDTO;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOptionRequestDTO;
import com.student.edsbackend.features.declaration.initial.dal.option.AdditionalAnswerOptionUpdateDTO;

import java.util.Optional;

public interface AdditionalAnswerOptionService {

    /**
     * Get a specific additional answer option by ID if not deleted
     */
    Optional<AdditionalAnswerOptionDTO> getAdditionalAnswerOptionById(Integer id);

    /**
     * Create a new additional answer option
     */
    AdditionalAnswerOptionDTO createAdditionalAnswerOption(AdditionalAnswerOptionRequestDTO request);

    /**
     * Update an existing additional answer option
     */
    AdditionalAnswerOptionDTO updateAdditionalAnswerOption(Integer id, AdditionalAnswerOptionUpdateDTO request);

    /**
     * Soft delete an additional answer option by setting isDeleted to true
     */
    void deleteAdditionalAnswerOption(Integer id);
}
