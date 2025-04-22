package com.student.edsbackend.features.declaration.agreement.service;

import com.student.edsbackend.features.declaration.agreement.dto.UserDecAgreementAnswerDeclareDTO;
import com.student.edsbackend.features.declaration.agreement.dto.UserDecAgreementAnswerExcludeDTO;
import com.student.edsbackend.features.declaration.agreement.dto.UserDecAgreementAnswerRequestDTO;

import java.util.List;

public interface UserDecAgreementAnswerService {

    /**
     * Create one or more agreement answers (returns the first saved as a DeclareDTO).
     */
    UserDecAgreementAnswerDeclareDTO create(UserDecAgreementAnswerRequestDTO requestDTO);

    /**
     * Admin-only: fetch all "declare" answers.
     */
    List<UserDecAgreementAnswerDeclareDTO> getAllDeclare();

    /**
     * Admin-only: fetch all "exclude" answers.
     */
    List<UserDecAgreementAnswerExcludeDTO> getAllExclude();

    /**
     * Fetch a single declare answer by its ID.
     */
    UserDecAgreementAnswerDeclareDTO getById(Integer id);

    /**
     * Fetch all declare answers for a given user.
     */
    List<UserDecAgreementAnswerDeclareDTO> getByUserId(Integer userId);

    /**
     * Fetch all declare answers associated with a specific Ad-Hoc Declare.
     */
    List<UserDecAgreementAnswerDeclareDTO> getAdHocDeclareAnswerByUserId(Integer adHocDeclareAnswerId);

    /**
     * Fetch all exclude answers associated with a specific Ad-Hoc Exclude.
     */
    List<UserDecAgreementAnswerExcludeDTO> getAdHocExcludeAnswerByUserId(Integer adHocExcludeId);

    /**
     * Soft-delete an answer (mark isDeleted = true).
     */
    boolean delete(Integer id);
}
