package com.student.edsbackend.features.declaration.initial.service;

import java.util.List;
import java.util.Optional;

import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationDTO;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationRequestDTO;

public interface InitialDeclarationService {

    List<InitialDeclarationDTO> getAllDeclarations();

    Optional<InitialDeclarationDTO> getDeclarationById(Integer id);

    InitialDeclarationDTO createDeclaration(InitialDeclarationRequestDTO declaration);

    InitialDeclarationDTO updateDeclaration(Integer id, InitialDeclarationRequestDTO declaration);

    void deleteDeclaration(Integer id);
}
