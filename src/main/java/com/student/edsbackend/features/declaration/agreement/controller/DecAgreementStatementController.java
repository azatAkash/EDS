package com.student.edsbackend.features.declaration.agreement.controller;

import com.student.edsbackend.features.declaration.agreement.dto.DecAgreementStatementDTO;
import com.student.edsbackend.features.declaration.agreement.dto.DecAgreementStatementRequestDTO;
import com.student.edsbackend.features.declaration.agreement.dto.DecAgreementStatementUpdateResponseDTO;
import com.student.edsbackend.features.declaration.agreement.service.DecAgreementStatementService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/declaration/agreement/statements")
@RequiredArgsConstructor
@Tag(name = "Declaration Agreement Statements", description = "Endpoints for managing declaration agreement statements")
public class DecAgreementStatementController {

    private final DecAgreementStatementService service;

    @PostMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<DecAgreementStatementDTO> create(@RequestBody DecAgreementStatementRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<List<DecAgreementStatementDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<DecAgreementStatementDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<DecAgreementStatementUpdateResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody DecAgreementStatementRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (service.delete(id)) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }
}