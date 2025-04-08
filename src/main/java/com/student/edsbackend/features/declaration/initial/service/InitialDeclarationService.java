package com.student.edsbackend.features.declaration.initial.service;

import java.util.List;
import java.util.Optional;

import com.student.edsbackend.features.declaration.initial.dal.InitialDeclaration;
import com.student.edsbackend.features.declaration.initial.dal.InitialDeclarationDTO;

public interface InitialDeclarationService {
    List<InitialDeclarationDTO> getAllDeclarations();
    
    Optional<InitialDeclarationDTO> getDeclarationById(Integer id);
    
    InitialDeclarationDTO createDeclaration(InitialDeclaration declaration);
    
    // InitialDeclaration updateDeclaration(Integer id, InitialDeclaration declaration);
    
    void deleteDeclaration(Integer id);
}
