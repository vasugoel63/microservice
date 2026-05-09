package com.example.billservice.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FailPaymentRequest {
    private String razorpay_order_id;
    private String razorpay_payment_id;
    private BigDecimal amount;
    private String paymentMethod;
    private String billingAccountId;
    private String patientId;
    private String failure_reason;
}
