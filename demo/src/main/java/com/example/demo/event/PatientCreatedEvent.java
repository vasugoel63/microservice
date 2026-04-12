package com.example.demo.event;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PatientCreatedEvent {
    private String patientId;
    private String name;
    private String email;
}
