/*package com.projectalpha.service.notification.impl;

import com.projectalpha.dto.notification.NotificationDTO;
import com.projectalpha.dto.notification.NotificationType;
import com.projectalpha.repository.notification.NotificationRepository;
import com.projectalpha.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        return notificationRepository.save(notificationDTO);
    }

    @Override
    public Optional<NotificationDTO> getNotificationById(UUID notificationId) {
        return notificationRepository.findById(notificationId);
    }

    @Override
    public List<NotificationDTO> getNotificationsByUser(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public List<NotificationDTO> getUnreadNotificationsByUser(String userId) {
        return notificationRepository.findUnreadByUserId(userId);
    }

    @Override
    public void markAsRead(UUID notificationId) {
        notificationRepository.markAsRead(notificationId);
    }

    @Override
    public void markAllAsRead(String userId) {
        notificationRepository.markAllAsRead(userId);
    }

    @Override
    public void deleteNotification(UUID notificationId) {
        notificationRepository.delete(notificationId);
    }
}*/