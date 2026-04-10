package com.example.demo.KafkaProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.event.PatientCreatedEvent;

@Service
public class PatientKafkaProducer {

    @Autowired
    private KafkaTemplate<String, PatientCreatedEvent> kafkaTemplate;

    public void sendPatientCreatedEvent(PatientCreatedEvent event) {
        kafkaTemplate.send("patient-created-topic", event);
    }
}