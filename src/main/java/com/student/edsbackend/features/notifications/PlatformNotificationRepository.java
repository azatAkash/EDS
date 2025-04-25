package com.student.edsbackend.features.notifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.student.edsbackend.features.user.dal.User;

import java.util.List;

@Repository
public interface PlatformNotificationRepository extends JpaRepository<platformNotification, Integer> {
    
    /**
     * Find all notifications for a specific user
     * @param user The user to find notifications for
     * @return List of notifications
     */
    List<platformNotification> findByReceiverOrderByCreationDateDesc(User receiver);
    
    /**
     * Find all unread notifications for a specific user
     * @param user The user to find notifications for
     * @param isRead The read status (false for unread)
     * @return List of unread notifications
     */
    List<platformNotification> findByReceiverAndIsReadOrderByCreationDateDesc(User receiver, Boolean isRead);
    
    /**
     * Count unread notifications for a specific user
     * @param user The user to count notifications for
     * @param isRead The read status (false for unread)
     * @return Count of unread notifications
     */
    Long countByReceiverAndIsRead(User receiver, Boolean isRead);
    
    /**
     * Find all non-deleted notifications for a specific user
     * @param user The user to find notifications for
     * @param isDeleted The deletion status (false for not deleted)
     * @return List of non-deleted notifications
     */
    List<platformNotification> findByReceiverAndIsDeletedOrderByCreationDateDesc(User receiver, Boolean isDeleted);
    
    /**
     * Find all non-deleted notifications for a specific user with a specific read status
     * @param user The user to find notifications for
     * @param isRead The read status
     * @param isDeleted The deletion status (false for not deleted)
     * @return List of notifications matching criteria
     */
    List<platformNotification> findByReceiverAndIsReadAndIsDeletedOrderByCreationDateDesc(User receiver, Boolean isRead, Boolean isDeleted);
}