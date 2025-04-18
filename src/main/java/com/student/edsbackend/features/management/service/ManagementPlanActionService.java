package com.student.edsbackend.features.management.service;

import java.util.List;

import com.student.edsbackend.features.management.dto.ManagementPlanActionDTO;
import com.student.edsbackend.features.management.dto.ManagementPlanActionRequestDTO;

public interface ManagementPlanActionService {
    ManagementPlanActionDTO create(ManagementPlanActionRequestDTO requestDTO);
    List<ManagementPlanActionDTO> getAll();
    ManagementPlanActionDTO getById(Integer id);
    boolean delete(Integer id);
}
