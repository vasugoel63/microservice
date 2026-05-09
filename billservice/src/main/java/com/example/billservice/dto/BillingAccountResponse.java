package com.example.billservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingAccountResponse {

    private String id;
    private String patientId;
    private Double balance;

    // getters & setters
}