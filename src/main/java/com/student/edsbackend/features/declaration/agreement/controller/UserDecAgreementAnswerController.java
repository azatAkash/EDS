package com.student.edsbackend.features.declaration.agreement.controller;

import com.student.edsbackend.features.declaration.agreement.dto.UserDecAgreementAnswerDeclareDTO;
import com.student.edsbackend.features.declaration.agreement.dto.UserDecAgreementAnswerExcludeDTO;
import com.student.edsbackend.features.declaration.agreement.dto.UserDecAgreementAnswerRequestDTO;
import com.student.edsbackend.features.declaration.agreement.service.UserDecAgreementAnswerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/adhoc-declarations/agreement-answers")
@RequiredArgsConstructor
@Tag(name = "User Declaration Agreement Answers", description = "Endpoints for managing user declaration agreement answers")
public class UserDecAgreementAnswerController {

    private final UserDecAgreementAnswerService service;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','MANAGER','USER')")
    public ResponseEntity<UserDecAgreementAnswerDeclareDTO> create(
            @RequestBody UserDecAgreementAnswerRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/declare")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','MANAGER')")
    public ResponseEntity<List<UserDecAgreementAnswerDeclareDTO>> getAllDeclare() {
        return ResponseEntity.ok(service.getAllDeclare());
    }

    @GetMapping("/exclude")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','MANAGER')")
    public ResponseEntity<List<UserDecAgreementAnswerExcludeDTO>> getAllExclude() {
        return ResponseEntity.ok(service.getAllExclude());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','MANAGER','USER')")
    public ResponseEntity<UserDecAgreementAnswerDeclareDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/user/{userId}/declare")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','MANAGER','USER')")
    public ResponseEntity<List<UserDecAgreementAnswerDeclareDTO>> getDeclareByUser(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    @GetMapping("/ad-hoc-declare/{adHocDeclareAnswerId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','MANAGER','USER')")
    public ResponseEntity<List<UserDecAgreementAnswerDeclareDTO>> getByAdHocDeclare(
            @PathVariable Integer adHocDeclareAnswerId) {
        return ResponseEntity.ok(service.getAdHocDeclareAnswerByUserId(adHocDeclareAnswerId));
    }

    @GetMapping("/ad-hoc-exclude/{adHocExcludeId}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','MANAGER','USER')")
    public ResponseEntity<List<UserDecAgreementAnswerExcludeDTO>> getByAdHocExclude(
            @PathVariable Integer adHocExcludeId) {
        return ResponseEntity.ok(service.getAdHocExcludeAnswerByUserId(adHocExcludeId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return service.delete(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
