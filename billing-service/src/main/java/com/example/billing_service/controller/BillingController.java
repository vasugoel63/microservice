package com.example.billing_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.billing_service.entities.BillingAccount;
import com.example.billing_service.service.BillingService;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<BillingAccount> fetchBalance(@PathVariable String patientId) {
        BillingAccount billingAccount = billingService.getAccount(patientId);
        return ResponseEntity.ok(billingAccount);
    }
}
