package com.example.billing_service.service;

import org.springframework.stereotype.Service;
import com.example.billing_service.entities.BillingAccount;
import com.example.billing_service.repository.BillingRepository;

@Service
public class BillingService {

    private final BillingRepository billingRepository;

    public BillingService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public BillingAccount createAccount(String patientId, String name, String email) {
        BillingAccount account = new BillingAccount();
        account.setPatientId(patientId);
        account.setEmail(email);
        account.setStatus("ACTIVE");
        account.setName(name);
        account.setBalance(0.0);
        return billingRepository.save(account);
    }

    public BillingAccount getAccount(String patientId) {
        return billingRepository.findByPatientId(patientId)
                .orElse(null);
    }

    public void updateBalance(String patientId, Double amount) {
        BillingAccount account = billingRepository.findByPatientId(patientId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance() + amount);

        billingRepository.save(account);
    }
}