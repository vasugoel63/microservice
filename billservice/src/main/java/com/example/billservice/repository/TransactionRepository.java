package com.example.billservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.billservice.entities.Bill;
import com.example.billservice.entities.LineItem;
import com.example.billservice.entities.Transaction;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByBillingAccountId(String billingAccountId);

    List<Transaction> findByBill_Id(UUID billId);
}