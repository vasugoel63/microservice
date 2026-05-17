package com.example.demo.KafkaProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.event.NotificationEvent;
import com.example.demo.event.PatientCreatedEvent;

@Service
public class PatientKafkaProducer {

    @Autowired
    private KafkaTemplate<String, PatientCreatedEvent> patientKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, NotificationEvent> notificationKafkaTemplate;

    public void sendPatientCreatedEvent(PatientCreatedEvent event) {
        System.out.println("🔥 BILLING EVENT SENT: " + event);
        patientKafkaTemplate.send("patient-created-topic", event);
        NotificationEvent event2 = new NotificationEvent();
        event2.setMessage("Patient Created successfully named " + event.getName());
        event2.setTitle("Patient Created");
        event2.setUserid(event.getPatientId());
        System.out.println("🔥 NOTI SENT EVENT RECIEVED: " + event2);
        notificationKafkaTemplate.send("all-event-tracker", event2);
    }

}