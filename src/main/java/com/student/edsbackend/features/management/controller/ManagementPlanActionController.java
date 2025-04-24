package com.student.edsbackend.features.management.controller;

import com.student.edsbackend.features.management.dto.ManagementPlanActionDTO;
import com.student.edsbackend.features.management.dto.ManagementPlanActionRequestDTO;
import com.student.edsbackend.features.management.service.ManagementPlanActionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/management-plans/actions")
@RequiredArgsConstructor
@Tag(name = "Management Plan Actions", description = "Endpoints for managing plan actions")
public class ManagementPlanActionController {

    private final ManagementPlanActionService service;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ManagementPlanActionDTO> create(@RequestBody ManagementPlanActionRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'USER', 'MANAGER')")
    public ResponseEntity<List<ManagementPlanActionDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'USER', 'MANAGER')")
    public ResponseEntity<ManagementPlanActionDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (service.delete(id)) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }
}
