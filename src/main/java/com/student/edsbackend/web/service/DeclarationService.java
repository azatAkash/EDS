package com.student.edsbackend.web.service;

import com.student.edsbackend.dal.declaration.DeclarationDTO;
import java.util.List;

public interface DeclarationService {

    /**
     * Returns the declaration for the currently authenticated user. Expects the
     * user's ID to be available from the security context.
     */
    DeclarationDTO findDeclarationForUser();

    /**
     * Returns all declarations for an admin user.
     */
    List<DeclarationDTO> findAllForAdmin();

    void createDeclaration(DeclarationDTO dto);

    void updateDeclaration(DeclarationDTO dto);

    void deleteDeclaration(Integer id);
}
