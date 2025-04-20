package com.student.edsbackend.features.declaration.agreement.service.implementation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.student.edsbackend.configs.JsonConverter;
import com.student.edsbackend.features.declaration.agreement.DecAgreementStatement;
import com.student.edsbackend.features.declaration.agreement.dto.DecAgreementStatementDTO;
import com.student.edsbackend.features.declaration.agreement.dto.DecAgreementStatementRequestDTO;
import com.student.edsbackend.features.declaration.agreement.dto.DecAgreementStatementUpdateResponseDTO;
import com.student.edsbackend.features.declaration.agreement.repository.DecAgreementStatementRepository;
import com.student.edsbackend.features.declaration.agreement.service.DecAgreementStatementService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DecAgreementStatementServiceImpl implements DecAgreementStatementService {

    private final DecAgreementStatementRepository repository;

    @Override
    public DecAgreementStatementDTO create(DecAgreementStatementRequestDTO requestDTO) {
        DecAgreementStatement statement = DecAgreementStatement.builder()
                .description(JsonConverter.ensureLangs(requestDTO.getDescription()))
                .isDeleted(false)
                .build();

        return toDTO(repository.save(statement));
    }

    @Override
    public List<DecAgreementStatementDTO> getAll() {
        return repository.findByIsDeletedFalse()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DecAgreementStatementDTO getById(Integer id) {
        DecAgreementStatement statement = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agreement statement not found"));
        return toDTO(statement);
    }

    @Override
    public boolean delete(Integer id) {
        DecAgreementStatement statement = repository.findByIdAndIsDeletedFalse(id)
                .orElse(null);
        if (statement == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such Agreement statement to delete");
        }
        statement.setIsDeleted(true);
        repository.save(statement);
        return true;
    }

    @Override
public DecAgreementStatementUpdateResponseDTO update(Integer id, DecAgreementStatementRequestDTO requestDTO) {
    DecAgreementStatement statement = repository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agreement statement not found"));

    Map<String, String> oldDescription = statement.getDescription();
    Map<String, String> updatedDescription = JsonConverter.ensureLangs(requestDTO.getDescription());

    statement.setDescription(updatedDescription);
    statement = repository.save(statement);

    return DecAgreementStatementUpdateResponseDTO.builder()
            .id(statement.getId())
            .isDeleted(statement.getIsDeleted())
            .oldDescription(oldDescription)
            .newDescription(updatedDescription)
            .build();
}

    private DecAgreementStatementDTO toDTO(DecAgreementStatement statement) {
        return DecAgreementStatementDTO.builder()
                .id(statement.getId())
                .description(statement.getDescription())
                .isDeleted(statement.getIsDeleted())
                .build();
    }
}