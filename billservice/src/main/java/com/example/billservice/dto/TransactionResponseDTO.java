package com.example.billservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponseDTO {
    private UUID id;
    private BigDecimal amount;
    private String billingAccountId;
    private UUID billId;
    private String billAQId;
    private String status;
    private String paymentMethod;
    private String transactionRefId;
    private String razorpay_order_id;
    private String razorpay_payment_id;
    private String failureReason;
    private LocalDateTime createdAt;

}
