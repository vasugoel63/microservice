package com.example.billing_service.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillUpdateEvent {
    private String patientId;
    private Double amount;
}