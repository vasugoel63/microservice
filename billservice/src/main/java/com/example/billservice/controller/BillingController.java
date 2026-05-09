package com.example.billservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.billservice.dto.BillRequestDTO;
import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.entities.Bill;
import com.example.billservice.entities.Transaction;
import com.example.billservice.services.BillService;

@RestController
@RequestMapping("/api/bill")
public class BillingController {

    private final BillService billService;

    public BillingController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping
    public BillResponseDTO createBill(@RequestBody BillRequestDTO request) {
      
        return billService.createBill(request);
    }

    @GetMapping
    public List<Bill> getBills() {
        return billService.getBills();
    }

    @GetMapping("/{billId}")
    public BillResponseDTO getSingleBill(@PathVariable UUID billId) {
        return billService.getBill(billId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Bill> getBillsByPatient(@PathVariable String patientId) {
        return billService.getBillByPatient(patientId);
    }

    @PutMapping("/{billId}")
    public Bill updateBillStatus(@PathVariable UUID billId,
            @RequestParam String status) {
        return billService.updateStatus(billId, status);
    }

    @DeleteMapping("/{billId}")
    public void deleteBill(@PathVariable UUID billId) {
        billService.deleteBill(billId);
    }
}
