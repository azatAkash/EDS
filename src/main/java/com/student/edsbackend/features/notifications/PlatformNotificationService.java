package com.student.edsbackend.features.notifications;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.student.edsbackend.features.user.dal.User;
import com.student.edsbackend.features.user.dal.UserDTO;
import com.student.edsbackend.features.user.dal.UserRepository;
import com.student.edsbackend.features.user.service.UserService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlatformNotificationService {

    private final PlatformNotificationRepository notificationRepository;
    private final UserRepository userRepository;
    
    @Value("${app.base-url}")
    private String baseUrl;
    
    /**
     * Create a new notification
     * 
     * @param userEmail The email of the user to receive the notification
     * @param description The notification description text
     * @return The created notification DTO
     */
    @Transactional
    public PlatformNotificationDTO createNotification(String userEmail, String description) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        platformNotification notification = platformNotification.builder()
                .description(description)
                .receiver(user)
                .creationDate(LocalDateTime.now())
                .isRead(false)
                .isDeleted(false)
                .build();
        System.out.println("Notification: " + notification);

        platformNotification savedNotification = notificationRepository.save(notification);
        return mapToDTO(savedNotification, countUnreadNotifications(user));
    }
    
    /**
     * Create a notification with a link in the description
     * 
     * @param userId The ID of the user to receive the notification
     * @param text The text part of the description
     * @param linkText The text to display for the link
     * @param linkPath The path to append to the base URL
     * @return The created notification DTO
     */
    @Transactional
    public PlatformNotificationDTO createNotificationWithLink(String userEmail, String text, String linkText, String linkPath) {
        String fullLink = baseUrl + (linkPath.startsWith("/") ? linkPath : "/" + linkPath);
        String description = text + " <a href=\"" + fullLink + "\">" + linkText + "</a>";
        return createNotification(userEmail, description);
    }
    
    /**
     * Get all notifications for a user
     * 
     * @param userId The ID of the user
     * @param onlyUnread If true, return only unread notifications
     * @return List of notification DTOs
     */
    public List<PlatformNotificationDTO> getUserNotifications(String userEmail, boolean onlyUnread) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<platformNotification> notifications;
        if (onlyUnread) {
            notifications = notificationRepository.findByReceiverAndIsReadAndIsDeletedOrderByCreationDateDesc(user, false, false);
        } else {
            notifications = notificationRepository.findByReceiverAndIsDeletedOrderByCreationDateDesc(user, false);
        }
        
        Long unreadCount = countUnreadNotifications(user);
        return notifications.stream()
                .map(notification -> mapToDTO(notification, unreadCount))
                .collect(Collectors.toList());
    }
    
    /**
     * Mark a notification as read
     * 
     * @param notificationId The ID of the notification
     * @param userId The ID of the user (for security check)
     * @return The updated notification DTO
     */
    @Transactional
    public PlatformNotificationDTO markAsRead(Integer notificationId, String userEmail) {
        platformNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        // Security check - ensure the notification belongs to the user
        if (!notification.getReceiver().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized access to notification");
        }
        
        notification.setIsRead(true);
        platformNotification savedNotification = notificationRepository.save(notification);
        
        return mapToDTO(savedNotification, countUnreadNotifications(notification.getReceiver()));
    }
    
    /**
     * Mark all notifications for a user as read
     * 
     * @param userId The ID of the user
     * @return The number of notifications marked as read
     */
    @Transactional
    public int markAllAsRead(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<platformNotification> unreadNotifications = 
                notificationRepository.findByReceiverAndIsReadAndIsDeletedOrderByCreationDateDesc(user, false, false);
        
        unreadNotifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(unreadNotifications);
        
        return unreadNotifications.size();
    }
    
    /**
     * Delete a notification (soft delete)
     * 
     * @param notificationId The ID of the notification
     * @param userId The ID of the user (for security check)
     * @return True if deleted successfully
     */
    @Transactional
    public boolean deleteNotification(Integer notificationId, Integer userId) {
        platformNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        // Security check - ensure the notification belongs to the user
        if (!notification.getReceiver().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to notification");
        }
        
        notification.setIsDeleted(true);
        notificationRepository.save(notification);
        
        return true;
    }
    
    /**
     * Count unread notifications for a user
     * 
     * @param user The user
     * @return Count of unread notifications
     */
    private Long countUnreadNotifications(User receiver) {
        return notificationRepository.countByReceiverAndIsRead(receiver, false);
    }
    
    /**
     * Map a notification entity to a DTO
     * 
     * @param notification The notification entity
     * @param unreadCount The count of unread notifications
     * @return The notification DTO
     */
    private PlatformNotificationDTO mapToDTO(platformNotification notification, Long unreadCount) {
        User user = notification.getReceiver();
        
        return PlatformNotificationDTO.builder()
                .id(notification.getId())
                .description(notification.getDescription())
                .receiver(mapToUserDTO(user))
                .creationDate(notification.getCreationDate())
                .isRead(notification.getIsRead())
                .unreadCount(unreadCount)
                .build();
    }

    private UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .registrationDate(user.getRegistrationDate())
                .department(user.getDepartment())
                .email(user.getEmail())
                .lastname(user.getLastname())
                .position(user.getPosition())
                .isDeleted(user.getIsDeleted())
                .middlename(user.getMiddlename())
                .isActive(user.getIsActive())
                .build();
    }
}