package com.student.edsbackend.features.declaration.answers;


import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationDetailedResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class UserDeclarationPdfGenerator {

    private final TemplateEngine templateEngine;

    public UserDeclarationPdfGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void generatePdfToHttpResponse(HttpServletResponse response, UserDeclarationDetailedResponseDTO dto) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=User_Declaration.pdf");

        Context context = new Context();
        context.setVariable("declaration", dto);

        String htmlContent = templateEngine.process("declaration-pdf", context);


// 👇 Добавь это временно
System.out.println("=== HTML CONTENT ===");
System.out.println(htmlContent);
System.out.println("====================");


        try (OutputStream os = response.getOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, "");
            builder.toStream(os);
            builder.run();
        }
    }
} 
