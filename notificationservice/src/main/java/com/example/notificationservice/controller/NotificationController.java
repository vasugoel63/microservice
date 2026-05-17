package com.example.notificationservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.service.NotificationService;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Notification>> getNotifications() {
        List<Notification> notifications = notificationService.getLatestNotifications();
        return ResponseEntity.ok().body(notifications);
    }

    @PutMapping("/{id}/seen")
    public ResponseEntity<String> updateSeenStatus(@PathVariable UUID id) {

        notificationService.updateseenNotification(id);

        return ResponseEntity.ok("Notification marked as seen");
    }

}
