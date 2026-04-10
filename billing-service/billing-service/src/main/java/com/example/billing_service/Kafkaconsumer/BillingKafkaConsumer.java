package com.example.billing_service.Kafkaconsumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.billing_service.Event.PatientCreatedEvent;
import com.example.billing_service.service.BillingService;

@Service
public class BillingKafkaConsumer {

    private final BillingService billingService;

    public BillingKafkaConsumer(BillingService billingService) {
        this.billingService = billingService;
    }

    @KafkaListener(topics = "patient-created-topic", groupId = "billing-group")
    public void consume(PatientCreatedEvent event) {

        billingService.createAccount(
                event.getPatientId(),
                event.getName(),
                event.getEmail());
    }

    @KafkaListener(topics = "bill-created-topic", groupId = "billing-group")
    public void handleBillCreated(BillUpdateEvent event) {

        billingService.updateBalance(
                event.getPatientId(),
                event.getAmount()
        );
    }
}