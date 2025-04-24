package com.student.edsbackend.features.declaration.answers.service.implementations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationDetailedResponseDTO;
import com.student.edsbackend.features.enums.ManagementPlanStatus;
import com.student.edsbackend.features.enums.UserDeclarationStatus;
import com.student.edsbackend.features.management.UserManagementPlan;
import com.student.edsbackend.features.management.dto.UserManagementPlanDTO;

// В начале класса
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

@Service
public class PdfGeneratorService {

    private final TemplateEngine templateEngine;

    public PdfGeneratorService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    // …

    private String toDataUri(String classpathLocation) {
        try {
            ClassPathResource res = new ClassPathResource(classpathLocation);
            byte[] bytes = StreamUtils.copyToByteArray(res.getInputStream());
            String base64 = Base64.getEncoder().encodeToString(bytes);
            return "data:image/png;base64," + base64;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public byte[] generateInitialDeclarationPdf(UserDeclarationDetailedResponseDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("There is nothing to fill, data cannot be null");
        }

        if (dto.getStatus() == UserDeclarationStatus.CREATED && dto.getStatus() == UserDeclarationStatus.SENT_FOR_APPROVAL) {
            throw new IllegalArgumentException("Declaration is not submitted");
        }
        Context ctx = new Context();
        ctx.setVariable("declaration", dto);

        // Читаем из static/images:
        String checkedBox = toDataUri("static/images/checkedbox.png");
        String uncheckedBox = toDataUri("static/images/uncheckedbox.png");
        ctx.setVariable("checkedBox", checkedBox);
        ctx.setVariable("uncheckedBox", uncheckedBox);

        String html = templateEngine.process("declaration-form", ctx);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            // опционально: builder.useFastMode();
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Could not generate PDF", e);
        }
    }

}
