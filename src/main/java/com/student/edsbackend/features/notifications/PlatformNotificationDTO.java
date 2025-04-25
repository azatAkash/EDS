package com.student.edsbackend.features.notifications;

import java.time.LocalDateTime;

import com.student.edsbackend.features.user.dal.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for platform notifications
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformNotificationDTO {
    
    private Integer id;
    private String description;
    private UserDTO receiver;
    private LocalDateTime creationDate;
    private Boolean isRead;
    
    // Additional field for unread message count
    private Long unreadCount;
}