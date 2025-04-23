package com.student.edsbackend.features.declaration.answers.controller;

import com.student.edsbackend.features.ApiResponse;
import com.student.edsbackend.features.declaration.answers.UserDeclarationPdfGenerator;
import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationAnswerRequestDTO;
import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationAnswerResponseDTO;
import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationDetailedResponseDTO;
import com.student.edsbackend.features.declaration.answers.service.UserDeclarationAnswerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/declaration-answers")
@Tag(name = "User Declaration Answers", description = "Endpoints for managing user declaration answers")
public class UserDeclarationAnswerController {

    private final UserDeclarationAnswerService userDeclarationAnswerService;
    private final UserDeclarationPdfGenerator pdfGenerator;

    @PostMapping
    @Operation(summary = "Save user declaration answers", description = "Saves the user's answers to the initial declaration questions. User is identified from the security context.")
    @PreAuthorize("hasAnyAuthority('USER', 'SUPER_ADMIN')")
    public ResponseEntity<UserDeclarationAnswerResponseDTO> saveUserDeclarationAnswers(
            @RequestBody UserDeclarationAnswerRequestDTO requestDTO) {
        UserDeclarationAnswerResponseDTO responseDTO = userDeclarationAnswerService
                .saveUserDeclarationAnswers(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get declaration answers by user id", description = "Retrieves the answers to the initial declaration questions by user id. User is identified from the security context.")
    @PreAuthorize("hasAnyAuthority('USER', 'SUPER_ADMIN', 'ADMIN', 'MANAGER')")
    public ResponseEntity<UserDeclarationDetailedResponseDTO> getCurrentUserDeclarationAnswers(
            @PathVariable Integer id) {
        UserDeclarationDetailedResponseDTO responseDTO = userDeclarationAnswerService
                .getDeclarationAnswersByUserId(id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a declaration answer by ID", description = "Deletes a declaration answer by its ID. Only the owner, creator, or admin/super admin can delete.")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> deleteAnswerById(@PathVariable Integer id) {
        userDeclarationAnswerService.deleteUserDeclarationAnswerById(id);
        return ResponseEntity.ok(new ApiResponse("Answer deleted successfully"));
    }

    @GetMapping("/{id}/pdf")
    public void downloadPdf(@PathVariable Integer id, HttpServletResponse response) throws Exception {
        UserDeclarationDetailedResponseDTO responseDTO = userDeclarationAnswerService
                .getDeclarationAnswersByUserId(id);
        pdfGenerator.generatePdfToHttpResponse(response, responseDTO);
    }

    @GetMapping("/declaration-preview/{id}")
public String previewDeclaration(@PathVariable Integer id, Model model) {
    UserDeclarationDetailedResponseDTO declarationData = userDeclarationAnswerService.getDeclarationAnswersByUserId(id);
    model.addAttribute("declaration", declarationData);
    return "declaration-pdf";
}


    // @GetMapping("/{id}")
    // @Operation(summary = "Get declaration answer by ID",
    // description = "Retrieves a declaration answer by its ID. Only admin and super
    // admin can access.")
    // @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    // public ResponseEntity<UserDeclarationDetailedResponseDTO>
    // getUserDeclarationAnswerById(@PathVariable Integer id) {
    // UserDeclarationDetailedResponseDTO responseDTO = userDeclarationAnswerService
    // .getUserDeclarationAnswerById(id);
    // return ResponseEntity.ok(responseDTO);
    // }

    @DeleteMapping
    @Operation(summary = "Delete all current user's declaration answers", description = "Deletes all answers for the current user's declaration.")
    @PreAuthorize("hasAnyAuthority('USER', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> deleteCurrentUserDeclarationAnswers() {
        userDeclarationAnswerService.deleteCurrentUserDeclarationAnswers();
        return ResponseEntity.ok(new ApiResponse("All answers deleted successfully"));
    }

}