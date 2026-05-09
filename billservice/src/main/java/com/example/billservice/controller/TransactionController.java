package com.example.billservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.dto.TransactionResponseDTO;
import com.example.billservice.entities.Bill;
import com.example.billservice.services.TransactionService;
import com.example.billservice.entities.Transaction;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/bill")
public class TransactionController {

    private final TransactionService transactionservice;

    public TransactionController(TransactionService transactionService) {
        this.transactionservice = transactionService;
    }

    @GetMapping("/transactions")
    public List<TransactionResponseDTO> getTransactions() {
        return transactionservice.getTransactions();
    }

    @GetMapping("/transaction/bill/{billId}")
    public List<TransactionResponseDTO> getTransactionsByBill(@PathVariable UUID billId) {
        return transactionservice.getTransactionsByBill(billId);
    }

    @GetMapping("/transaction/billingaccount/{billingaccountid}")
    public List<Transaction> geTransactionsByAccount(@PathVariable String billingaccountid) {
        return transactionservice.getTransactionsByAccount(billingaccountid);
    }

}
