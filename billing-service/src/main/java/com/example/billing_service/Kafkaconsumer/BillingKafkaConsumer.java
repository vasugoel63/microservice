package com.example.billing_service.Kafkaconsumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.billing_service.Event.PatientCreatedEvent;
import com.example.billing_service.Event.BillUpdateEvent;
import com.example.billing_service.service.BillingService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BillingKafkaConsumer {

    private final BillingService billingService;

    public BillingKafkaConsumer(BillingService billingService) {
        this.billingService = billingService;
    }

    @KafkaListener(topics = "patient-created-topic", groupId = "billing-group")
    public void consume(PatientCreatedEvent event) {
        System.out.println(event);
        System.out.println("🔥 BILLING EVENT RECEIVED: " + event);

        billingService.createAccount(
                event.getPatientId(),
                event.getName(),
                event.getEmail());
    }
    @KafkaListener(topics = "patient-created-topic", groupId = "billing-group")
    public void consume(String message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        PatientCreatedEvent event = mapper.readValue(message, PatientCreatedEvent.class);

        billingService.createAccount(
                event.getPatientId(),
                event.getName(),
                event.getEmail());
    }
    @KafkaListener(topics = "bill-created-topic", groupId = "billing-group")
    public void handleBillCreated(String message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BillUpdateEvent event = mapper.readValue(message, BillUpdateEvent.class);

        billingService.updateBalance(
                event.getPatientId(),
                event.getAmount());
    }
    // @KafkaListener(topics = "bill-created-topic", groupId = "billing-group")
    // public void handleBillCreated(BillUpdateEvent event) {

    //     billingService.updateBalance(
    //             event.getPatientId(),
    //             event.getAmount());
    // }
}