package com.student.edsbackend.features.declaration.initial.service;

import java.util.List;
import java.util.Optional;

import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclaration;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationDTO;
import com.student.edsbackend.features.declaration.initial.dal.declaration_metadata.InitialDeclarationRequestDTO;
import com.student.edsbackend.features.user.dal.InitialDeclarationDetailedDTO;

public interface InitialDeclarationService {

    List<InitialDeclarationDTO> getAllDeclarations();

    Optional<InitialDeclarationDetailedDTO> getDeclarationById(Integer id);

    InitialDeclarationDTO createDeclaration(InitialDeclarationRequestDTO declaration);

    InitialDeclarationDTO activateDeclaration(Integer id);

    void deleteDeclaration(Integer id);
}
