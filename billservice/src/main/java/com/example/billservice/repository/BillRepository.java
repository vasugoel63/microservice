package com.example.billservice.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.billservice.entities.Bill;
import com.example.billservice.entities.BillStatus;

public interface BillRepository extends JpaRepository<Bill, UUID> {
    List<Bill> findByPatientId(String patientId, Sort sort);

    Bill findByRazorpayOrderId(String razorpayOrderId);;

    int countByPurchaseDateTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Bill> findByStatus(BillStatus status);
}
