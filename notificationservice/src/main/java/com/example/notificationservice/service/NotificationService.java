package com.example.notificationservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.event.NotificationEvent;
import com.example.notificationservice.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(NotificationEvent notificationCreatedEvent) {
        Notification notification = new Notification();
        notification.setIsRead(false);
        // notification.setCreatedAt();
        notification.setMessage(notificationCreatedEvent.getMessage());
        notification.setTitle(notificationCreatedEvent.getTitle());
        notification.setUserid(notificationCreatedEvent.getUserid());
        Notification savedNotification = notificationRepository.save(notification);
        return savedNotification;
    }

    public String updateseenNotification(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        return "Success";
    }

    public List<Notification> getLatestNotifications() {
        List<Notification> notifications = notificationRepository.findTop10ByOrderByCreatedAtDesc();
        return notifications;
    }
}