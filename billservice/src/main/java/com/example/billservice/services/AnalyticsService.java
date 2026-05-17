package com.example.billservice.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.billservice.dto.RevenueDetailsDTO;
import com.example.billservice.entities.Bill;
import com.example.billservice.entities.BillStatus;
import com.example.billservice.entities.Transaction;
import com.example.billservice.entities.TransactionStatus;
import com.example.billservice.repository.BillRepository;
import com.example.billservice.repository.TransactionRepository;

@Service
public class AnalyticsService {
    private BillRepository billRepository;
    private TransactionRepository transactionRepository;

    public AnalyticsService(BillRepository billRepository, TransactionRepository transactionRepository) {
        this.billRepository = billRepository;
        this.transactionRepository = transactionRepository;
    }

    public RevenueDetailsDTO getRevenuDetails() {
        List<Transaction> failedTransactions = transactionRepository.findByStatus(TransactionStatus.FAILED);
        int failcount = failedTransactions.size();
        List<Transaction> successTransactions = transactionRepository.findByStatus(TransactionStatus.SUCCESS);
        List<Transaction> pendingTransactions = transactionRepository.findByStatus(TransactionStatus.PENDING);
        int passcount = successTransactions.size();
        int pendingcount = pendingTransactions.size();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Transaction transaction : successTransactions) {
            totalAmount = totalAmount.add(transaction.getAmount());
        }
        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        int todayBillsCount = billRepository.countByPurchaseDateTimeBetween(
                startOfDay,
                endOfDay);
        List<Bill> pendingBills = billRepository.findByStatus(BillStatus.PENDING);
        int pendingbcount = pendingBills.size();
        return new RevenueDetailsDTO(passcount, failcount, pendingcount, totalAmount, todayBillsCount, pendingbcount);
    }
}
