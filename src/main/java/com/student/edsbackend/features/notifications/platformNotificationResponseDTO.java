package com.student.edsbackend.features.notifications;

import java.time.LocalDateTime;
import java.util.Map;

import com.student.edsbackend.features.user.dal.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for platform notifications
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class platformNotificationResponseDTO {
    private Integer id;
    private Map<String, String> description;
    private UserDTO reciever;
    private LocalDateTime creationDate;
    private Boolean isRead;
    
    // Additional field for unread message count
    private Long unreadCount;
}