package com.example.billservice.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionDTO {
    private BigDecimal amount;
    private String status;
    private String paymentMethod;
    private String billingAccountId;
}
