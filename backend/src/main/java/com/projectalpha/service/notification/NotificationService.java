/*package com.projectalpha.service.notification;

import com.projectalpha.dto.notification.NotificationDTO;
import com.projectalpha.dto.notification.NotificationType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationService {
    NotificationDTO createNotification(NotificationDTO notificationDTO);
    Optional<NotificationDTO> getNotificationById(UUID notificationId);
    List<NotificationDTO> getNotificationsByUser(String userId);
    List<NotificationDTO> getUnreadNotificationsByUser(String userId);
    void markAsRead(UUID notificationId);
    void markAllAsRead(String userId);
    void deleteNotification(UUID notificationId);
}*/