package com.example.billservice.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.kafka.common.protocol.types.Field.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.billservice.dto.FailPaymentRequest;
import com.example.billservice.dto.OrderRequest;
import com.example.billservice.dto.TransactionDTO;
import com.example.billservice.dto.VerifyPaymentRequest;
import com.example.billservice.services.PayService;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api/bill")
public class PaymentController {

    @Autowired
    private PayService payService;

    @PostMapping("/pay/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequest orderRequest)
            throws RazorpayException {
        Map<String, Object> response = payService.createOrder(orderRequest.getAmount(), orderRequest.getCurrency(),
                orderRequest.getBillId());
        return ResponseEntity.ok(response);
    }

    // verify payment
    @PostMapping("/pay/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody VerifyPaymentRequest request) throws RazorpayException {

        boolean isValid = payService.verifyPayment(request);
        // private BigDecimal amount;
        // private String status;
        // private String paymentMethod;
        // private String billingAccountId;
        if (isValid) {
            return ResponseEntity.ok("Payment Verified");
        }
        return ResponseEntity.badRequest().body("Invalid Payment");
    }

    @PostMapping("/pay/fail")
    public ResponseEntity<String> failedPayment(@RequestBody FailPaymentRequest request) throws RazorpayException {

        boolean isValid = payService.failPayment(request);
        // private BigDecimal amount;
        // private String status;
        // private String paymentMethod;
        // private String billingAccountId;
        if (isValid) {
            return ResponseEntity.ok(
                    "Failed transaction saved");
        }

        return ResponseEntity.badRequest()
                .body("Bill not found");
    }
}
