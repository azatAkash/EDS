package com.student.edsbackend.features.declaration.answers;


import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationDetailedResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;

@Component
public class UserDeclarationPdfGenerator {

    private final TemplateEngine templateEngine;

    public UserDeclarationPdfGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void generatePdfToHttpResponse(HttpServletResponse response, UserDeclarationDetailedResponseDTO dto) throws Exception {
        response.setContentType("application/pdf");
        String filename = "Declaration_" + dto.getUserDeclarationId() + ".pdf";
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
    
        // Сортировка по orderNumber (вопросы)
        dto.getQuestionsWithAnswers().sort(
            Comparator.comparing(UserDeclarationDetailedResponseDTO.QuestionWithAnswerDTO::getOrderNumber)
        );
    
        // Сортировка дополнительных ответов по orderIndex
        dto.getQuestionsWithAnswers().forEach(q -> {
            if (q.getOptionsWithAnswers() != null) {
                q.getOptionsWithAnswers().forEach(option -> {
                    if (option.getAdditionalAnswers() != null && option.getAdditionalAnswers().getAnswers() != null) {
                        option.getAdditionalAnswers().getAnswers()
                              .sort(Comparator.comparing(UserDeclarationDetailedResponseDTO.AdditionalAnswersGroupDTO::getOrderIndex));
                    }
                });
            }
        });
    
        Context context = new Context();
        context.setVariable("declaration", dto);
        context.setVariable("dateUtil", new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm"));
    
        String htmlContent = templateEngine.process("declaration-pdf", context);
    
        try (OutputStream os = response.getOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, "");
            builder.toStream(os);
            builder.run();
        }
    }
    
    
}
