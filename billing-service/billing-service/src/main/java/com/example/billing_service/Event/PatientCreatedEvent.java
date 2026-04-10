package com.example.billing_service.Event;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PatientCreatedEvent {
    private String patientId;
    private String name;
    private String email;
}
