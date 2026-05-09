package com.example.billservice.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    private int amount;
    private String currency;
    private UUID billId;
}
