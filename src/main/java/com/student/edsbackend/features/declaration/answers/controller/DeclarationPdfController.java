package com.student.edsbackend.features.declaration.answers.controller;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationDetailedResponseDTO;
import com.student.edsbackend.features.declaration.answers.service.UserDeclarationAnswerService;
import com.student.edsbackend.features.declaration.answers.service.implementations.PdfGeneratorService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/v1/export/initial-declaration")
public class DeclarationPdfController {

    private final UserDeclarationAnswerService answerService;
    private final PdfGeneratorService pdfService;

    public DeclarationPdfController(UserDeclarationAnswerService answerService,
                                    PdfGeneratorService pdfService) {
        this.answerService = answerService;
        this.pdfService = pdfService;
    }

    @GetMapping("/{id}/pdf")
    public void downloadPdf(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        // 1) Получаем DTO
        UserDeclarationDetailedResponseDTO dto = answerService.getDeclarationAnswersByDeclarationId(id);

        // 2) Генерируем PDF
        byte[] pdfBytes = pdfService.generatePdf(dto);

        // 3) Устанавливаем заголовки и пишем в поток
        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=declaration_" + id + ".pdf");
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }
}
