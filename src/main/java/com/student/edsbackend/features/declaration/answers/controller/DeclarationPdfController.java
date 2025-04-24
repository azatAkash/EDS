package com.student.edsbackend.features.declaration.answers.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationDetailedResponseDTO;
import com.student.edsbackend.features.declaration.answers.service.UserDeclarationAnswerService;
import com.student.edsbackend.features.declaration.answers.service.implementations.PdfGeneratorService;
import com.student.edsbackend.features.management.UserManagementPlan;
import com.student.edsbackend.features.management.dto.UserManagementPlanDTO;
import com.student.edsbackend.features.management.service.UserManagementPlanService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/v1/export")
public class DeclarationPdfController {

    private final UserDeclarationAnswerService answerService;
    private final PdfGeneratorService pdfService;
    private final UserManagementPlanService planService;
    public DeclarationPdfController(UserDeclarationAnswerService answerService,
                                    PdfGeneratorService pdfService, UserManagementPlanService planService) {
        this.answerService = answerService;
        this.pdfService = pdfService;
        this.planService = planService;
    }

    @GetMapping("initial-declaration/{id}/pdf")
    public void downloadInitialDeclarationPdf(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        // 1) Получаем DTO
        UserDeclarationDetailedResponseDTO dto = answerService.getDeclarationAnswersByDeclarationId(id);

        // 2) Генерируем PDF
        byte[] pdfBytes = pdfService.generateInitialDeclarationPdf(dto);

        // 3) Устанавливаем заголовки и пишем в поток
        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=declaration_" + id + ".pdf");
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }
    
}
