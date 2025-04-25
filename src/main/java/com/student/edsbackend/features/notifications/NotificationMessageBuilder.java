package com.student.edsbackend.features.notifications;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for building notification messages with links
 */
@Component
public class NotificationMessageBuilder {

    private final String baseUrl;

    public NotificationMessageBuilder(@Value("${app.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    /**
     * Build a notification message with a link
     * 
     * @param text The text part of the message
     * @param linkText The text to display for the link
     * @param linkPath The path to append to the base URL
     * @return The formatted message with HTML link
     */
    public String buildMessageWithLink(String text, String linkText, String linkPath) {
        String fullLink = baseUrl + (linkPath.startsWith("/") ? linkPath : "/" + linkPath);
        return text + " <a href=\"" + fullLink + "\">" + linkText + "</a>";
    }
    
    /**
     * Build a declaration notification message
     * 
     * @param declarationId The ID of the declaration
     * @param declarationName The name of the declaration
     * @return The formatted message with HTML link
     */
    public String buildDeclarationNotification(Integer declarationId, String declarationName) {
        String linkPath = "/declarations/" + declarationId;
        return buildMessageWithLink(
            "This declaration \"" + declarationName + "\" has been sent to you", 
            "link", 
            linkPath
        );
    }
}