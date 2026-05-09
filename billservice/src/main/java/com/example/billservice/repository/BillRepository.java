package com.example.billservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.billservice.entities.Bill;

public interface BillRepository extends JpaRepository<Bill, UUID> {
    List<Bill> findByPatientId(String patientId, Sort sort);

    Bill findByRazorpayOrderId(String razorpayOrderId);;
}
