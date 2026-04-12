package com.example.billservice.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillUpdateEvent {
    private String patientId;
    private Double amount;
}
