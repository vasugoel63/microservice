package com.example.billing_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.billing_service.entities.BillingAccount;

public interface BillingRepository extends JpaRepository<BillingAccount, UUID> {
    Optional<BillingAccount> findByPatientId(String patientId);

}
