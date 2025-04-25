package com.student.edsbackend.features.notifications;

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

        public String[] newDeclarationMessageText(String recipientEmail,
                        UserInitialDeclarationDTO dto) {
                String fullName = dto.getUser().getFirstname() + " " + dto.getUser().getLastname();
                String link = String.format("%s/initial-declaration", baseUrl);

                // Тема на трёх языках (same subject)
                String subject = String.join(" / ",
                                "New Conflict Resolution Declaration Assigned",
                                "Вам назначена декларация по разрешению конфликта",
                                "Сізге мүдделер қақтығыстарын шешу декларациясы тағайындалды");

                // Английский текст
                String bodyEn = String.format(
                                "Dear %s,\n\n" +
                                                "The Nazarbayev University Compliance Office has assigned a new Conflict Resolution Declaration to you. "
                                                +
                                                "Please complete the declaration by following this link: %s\n\n" +
                                                "If you have any questions, feel free to contact the Compliance Office.\n\n"
                                                +
                                                "Best regards,\n" +
                                                "Nazarbayev University Compliance Office",
                                fullName, link);

                

                // Собираем всё вместе с разделителями
                String body = String.join(
                                "\n––––––––––––––––––––––––––––––––––––––––––––––––\n\n",
                                bodyEn);

                return new String[] { subject, body };
        }

        public String[] newDeclrationMessage(String recipientEmail,
                        UserInitialDeclarationDTO dto) {
                String fullName = dto.getUser().getFirstname() + " " + dto.getUser().getLastname();
                String link = String.format("%s/initial-declaration", baseUrl);

                // Тема на трёх языках
                String subject = String.join(" / ",
                                "New Conflict Resolution Declaration Assigned",
                                "Вам назначена декларация по разрешению конфликта",
                                "Сізге мүдделер қақтығыстарын шешу декларациясы тағайындалды");

                // Английский блок
                String bodyEn = String.format(
                                "<p>Dear %s,</p>"
                                                + "<p>The Nazarbayev University Compliance Office has assigned a new Conflict Resolution Declaration to you. "
                                                + "Please complete the declaration by following this link: <a href=\"%s\">link</a></p>"
                                                + "<p>If you have any questions, feel free to contact the Compliance Office.</p>"
                                                + "<p>Best regards,<br>Nazarbayev University Compliance Office</p>",
                                fullName, link, link);

                // Русский блок
                String bodyRu = String.format(
                                "<p>Здравствуйте, %s!</p>"
                                                + "<p>Отдел комплаенса Назарбаев Университета назначил вам новую Декларацию по разрешению конфликта. "
                                                + "Пожалуйста, перейдите по этой ссылке для её заполнения: <a href=\"%s\">ссылка</a></p>"
                                                + "<p>Если у вас возникнут вопросы, обратитесь в Отдел комплаенса.</p>"
                                                + "<p>С уважением,<br>Отдел комплаенса Назарбаев Университета</p>",
                                fullName, link, link);

                // Казахский блок
                String bodyKk = String.format(
                                "<p>Құрметті %s,</p>"
                                                + "<p>Назарбаев Университетінің Комплаенс Офисы сізге жаңа мүдделер қақтығыстарын шешу декларациясын ұсынды. "
                                                + "Декларацияны<a href=\"%s\">Сілтеме</a> арқылы өтіп толтырыңыз.</p>"
                                                + "<p>Кез келген сұрақ туындаса, Комплаенс Офисына хабарласыңыз.</p>"
                                                + "<p>Құрметпен,<br>Назарбаев Университеті, Комплаенс Офисы</p>",
                                fullName, link);

                // Собираем финальное тело
                String body = "<html><body style=\"font-family:Arial,sans-serif; font-size:14px;\">"
                                + bodyEn
                                + "<hr/>"
                                + bodyRu
                                + "<hr/>"
                                + bodyKk
                                + "</body></html>";

                return new String[] { subject, body };
        }

        public void sendDeclarationAssigned(String recipientEmail, UserInitialDeclarationDTO dto)
                        throws MessagingException {
                String[] vals = newDeclrationMessage(recipientEmail, dto);

                sendHtmlMessage(recipientEmail, vals[0], vals[1]);
        }

        public void sendTest(String to) throws MessagingException {
                String link = String.format("%s/declarations/%d/fill", baseUrl, 1);

                String subject = "Test";
                String body = "Test";
                sendHtmlMessage(to, link, body);
        }
}
