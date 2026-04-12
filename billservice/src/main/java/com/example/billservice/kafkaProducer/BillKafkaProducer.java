package com.example.billservice.kafkaProducer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.billservice.event.BillUpdateEvent;

@Service
public class BillKafkaProducer {

    private final KafkaTemplate<String, BillUpdateEvent> kafkaTemplate;

    public BillKafkaProducer(KafkaTemplate<String, BillUpdateEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBillCreatedEvent(BillUpdateEvent event) {
        kafkaTemplate.send("bill-created-topic", event);
    }
}