package com.example.notificationservice.Kafkaconsumer;

import org.springframework.kafka.annotation.KafkaListener;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.event.NotificationEvent;
import com.example.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

@Component
public class NotificationKafkaConsumer {

   private final NotificationService notificationService;

   private final SimpMessagingTemplate messagingTemplate;

   public NotificationKafkaConsumer(NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
      this.notificationService = notificationService;
      this.messagingTemplate = messagingTemplate;
   }

   @KafkaListener(topics = "all-event-tracker", groupId = "billing-group")
   public void consume(NotificationEvent notificationcreatedEvent) {

      System.out.println("🔥 NOTI EVENT RECIEVED: " + notificationcreatedEvent);
      Notification savedNotification = notificationService.createNotification(event);
      messagingTemplate.convertAndSend(
            "/topic/notifications",
            savedNotification);
   }
}