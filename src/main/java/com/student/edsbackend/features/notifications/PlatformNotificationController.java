package com.student.edsbackend.features.notifications;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.student.edsbackend.features.ApiResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * REST controller for platform notifications
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class PlatformNotificationController {

    private final PlatformNotificationService notificationService;
    
    /**
     * Get all notifications for the current user
     * 
     * @param userId The ID of the user
     * @param onlyUnread If true, return only unread notifications
     * @return List of notification DTOs
     */
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<PlatformNotificationDTO>> getUserNotifications(
            @PathVariable String userEmail,
            @RequestParam(required = false, defaultValue = "false") boolean onlyUnread) {
        try {
            List<PlatformNotificationDTO> notifications = notificationService.getUserNotifications(userEmail, onlyUnread);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    
    /**
     * Mark a notification as read
     * 
     * @param notificationId The ID of the notification
     * @param userId The ID of the user
     * @return The updated notification DTO
     */
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<PlatformNotificationDTO> markAsRead(
            @PathVariable Integer notificationId,
            @RequestParam String userEmail) {
        try {
            PlatformNotificationDTO notification = notificationService.markAsRead(notificationId, userEmail);
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    
    /**
     * Mark all notifications for a user as read
     * 
     * @param userId The ID of the user
     * @return Response with the number of notifications marked as read
     */
    @PutMapping("/user/{userEmail}/read-all")
    public ResponseEntity<ApiResponse> markAllAsRead(@PathVariable String userEmail) {
        try {
            int count = notificationService.markAllAsRead(userEmail);
            return ResponseEntity.ok(new ApiResponse(count + " notifications marked as read"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    
    /**
     * Delete a notification (soft delete)
     * 
     * @param notificationId The ID of the notification
     * @param userId The ID of the user
     * @return Response indicating success
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse> deleteNotification(
            @PathVariable Integer notificationId,
            @RequestParam Integer userId) {
        try {
            boolean success = notificationService.deleteNotification(notificationId, userId);
            if (success) {
                return ResponseEntity.ok(new ApiResponse("Notification deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Failed to delete notification"));
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    
    /**
     * Get the count of unread notifications for a user
     * 
     * @param userId The ID of the user
     * @return Response with the count of unread notifications
     */
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<ApiResponse> getUnreadCount(@PathVariable String userEmail) {
        try {
            // Get any notification to get the unread count
            List<PlatformNotificationDTO> notifications = notificationService.getUserNotifications(userEmail, true);
            Long unreadCount = notifications.isEmpty() ? 0L : notifications.get(0).getUnreadCount();
            return ResponseEntity.ok(new ApiResponse(unreadCount.toString()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}