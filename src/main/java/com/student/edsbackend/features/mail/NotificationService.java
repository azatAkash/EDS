package com.student.edsbackend.features.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.student.edsbackend.features.declaration.answers.dto.UserDeclarationAnswerResponseDTO;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclaration;
import com.student.edsbackend.features.user.dal.UserDeclaration.UserInitialDeclarationDTO;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;
    private final String from;
    private final String baseUrl;

    public NotificationService(JavaMailSender mailSender,
            @Value("${spring.mail.username}") String from,
            @Value("${app.base-url}") String baseUrl) {
        this.mailSender = mailSender;
        this.from = from;
        this.baseUrl = baseUrl;
    }

    private void sendHtmlMessage(String to,
            String subject,
            String htmlBody) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();

        // Помечаем как очень важное
        msg.addHeader("X-Priority", "1"); // 1 (High) — 5 (Low)
        msg.addHeader("Priority", "urgent"); // или "high"
        msg.addHeader("Importance", "high");

        MimeMessageHelper helper = new MimeMessageHelper(msg, false, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        mailSender.send(msg);
    }

    public void sendDeclarationAssigned(String to,
                                        UserInitialDeclarationDTO dto) throws MessagingException {
        String link = String.format("%s/declarations/%d/fill", baseUrl, dto.getId());
        String firstName = dto.getUser().getFirstname();
        String lastName  = dto.getUser().getLastname();

        String subject;
        String body;


        subject = "Conflict resolution declaration has been assigned to you";


        body = String.format(
                    "<p>Dear %s %s,</p>"
                  + "<p>Nazarbayev University compliance office assigned a new intial declaration to you."
                  + "Please fill it out <a href=\"%s\">here</a>.</p>",
                    firstName, lastName, link
                + "<p>Уважаемый, %s %s!</p>"
                  + "<p>Комплаенс офис Назарбаев Университета назначил вам декларацию. "
                  + "Пожалуйста, заполните её <a href=\"%s\">здесь</a>.</p>",
                    firstName, lastName, link
                   + "<p>Құрметті, %s %s,</p>"
                  + "<p>Назарбаев Университінің Комплаенс Офисы сізге декларацияны тағайындады.</p>"
                  + "Оны мына <a href=\"%s\">сілтемеден</a> толтыра аласыз.</p>",
                    firstName, lastName, link
                );

        sendHtmlMessage(to, subject, body);
    }
}
