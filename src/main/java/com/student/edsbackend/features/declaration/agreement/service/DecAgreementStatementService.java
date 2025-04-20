package com.student.edsbackend.features.declaration.agreement.service;

import java.util.List;

import com.student.edsbackend.features.declaration.agreement.dto.DecAgreementStatementDTO;
import com.student.edsbackend.features.declaration.agreement.dto.DecAgreementStatementRequestDTO;
import com.student.edsbackend.features.declaration.agreement.dto.DecAgreementStatementUpdateResponseDTO;

public interface DecAgreementStatementService {
    /**
     * Create a new declaration agreement statement
     * @param requestDTO the DTO containing the data for the new statement
     * @return the created statement as a DTO
     */
    DecAgreementStatementDTO create(DecAgreementStatementRequestDTO requestDTO);
    
    /**
     * Get all non-deleted declaration agreement statements
     * @return a list of all non-deleted statements
     */
    List<DecAgreementStatementDTO> getAll();
    
    /**
     * Get a declaration agreement statement by ID
     * @param id the ID of the statement to retrieve
     * @return the statement as a DTO
     */
    DecAgreementStatementDTO getById(Integer id);
    
    /**
     * Delete a declaration agreement statement by ID
     * @param id the ID of the statement to delete
     * @return true if the statement was deleted, false if it was not found
     */
    boolean delete(Integer id);
    
    /**
     * Update a declaration agreement statement
     * @param id the ID of the statement to update
     * @param requestDTO the DTO containing the updated data
     * @return the updated statement as a DTO
     */
    DecAgreementStatementUpdateResponseDTO update(Integer id, DecAgreementStatementRequestDTO requestDTO);
}