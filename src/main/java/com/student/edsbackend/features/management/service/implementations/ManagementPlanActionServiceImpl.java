package com.student.edsbackend.features.management.service.implementations;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.student.edsbackend.configs.JsonConverter;
import com.student.edsbackend.features.management.ManagementPlanAction;
import com.student.edsbackend.features.management.dto.ManagementPlanActionDTO;
import com.student.edsbackend.features.management.dto.ManagementPlanActionRequestDTO;
import com.student.edsbackend.features.management.repository.ManagementPlanActionRepository;
import com.student.edsbackend.features.management.service.ManagementPlanActionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManagementPlanActionServiceImpl implements ManagementPlanActionService {

    private final ManagementPlanActionRepository repository;

    public ManagementPlanActionDTO create(ManagementPlanActionRequestDTO dto) {
    ManagementPlanAction action = ManagementPlanAction.builder()
            .description(JsonConverter.ensureLangsStrict(dto.getDescription())) // автоматически сохранится как JSON
            .isDeleted(false)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    

            
    return toDTO(repository.save(action));
}

    @Override
    public List<ManagementPlanActionDTO> getAll() {
        return repository.findByIsDeletedFalse()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ManagementPlanActionDTO getById(Integer id) {
        ManagementPlanAction action = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Action not found"));
        return toDTO(action);
    }

    @Override
    public boolean delete(Integer id) {
        ManagementPlanAction action = repository.findByIdAndIsDeletedFalse(id)
                .orElse(null);
        if (action == null) return false;
        action.setIsDeleted(true);
        action.setUpdatedAt(LocalDateTime.now());
        repository.save(action);
        return true;
    }

    private ManagementPlanActionDTO toDTO(ManagementPlanAction action) {
        return ManagementPlanActionDTO.builder()
                .id(action.getId())
                .description(action.getDescription())
                .isDeleted(action.getIsDeleted())
                .createdAt(action.getCreatedAt())
                .updatedAt(action.getUpdatedAt())
                .build();
    }
}
