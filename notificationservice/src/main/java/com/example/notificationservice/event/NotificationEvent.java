package com.example.notificationservice.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationEvent {
    public String message;
    public String userid;
    public String title;
}