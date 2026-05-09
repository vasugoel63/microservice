package com.example.billservice.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.example.billservice.dto.TransactionDTO;
import com.example.billservice.dto.TransactionResponseDTO;
import com.example.billservice.entities.Bill;
import com.example.billservice.entities.PaymentMethod;
import com.example.billservice.entities.Transaction;
import com.example.billservice.entities.TransactionStatus;
import com.example.billservice.mapper.BillMapper;
import com.example.billservice.repository.BillRepository;
import com.example.billservice.repository.TransactionRepository;
import com.google.type.DateTime;

import org.springframework.data.domain.Sort;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private BillRepository billRepository;

    public TransactionService(TransactionRepository transactionRepository, BillRepository billRepository) {
        this.transactionRepository = transactionRepository;
        this.billRepository = billRepository;
    }

    // public Transaction createTransaction(TransactionDTO transactionDTO, Bill bill) {

    //     Transaction transaction = new Transaction();
    //     transaction.setBill(bill);
    //     transaction.setAmount(transactionDTO.getAmount());
    //     transaction.setBillingAccountId(transactionDTO.getBillingAccountId());
    //     transaction.setTransactionRefId("TXN-" + System.currentTimeMillis());
    //     transaction.setPaymentMethod(PaymentMethod.UPI);
    //     transaction.setStatus(TransactionStatus.SUCCESS);
       
    //     Transaction savedTransaction = transactionRepository.save(transaction);
    //     // can be modified later to make bill paid after transaction happen
    //     return savedTransaction;
    // }

    public List<TransactionResponseDTO> getTransactions() {
        List<Transaction> transactions = transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return transactions.stream()
                .map(BillMapper::mapToDTO)
                .toList();
    }

    public List<Transaction> getTransactionsByAccount(String billingAccountId) {
        List<Transaction> transactionsbyaccount = transactionRepository.findByBillingAccountId(billingAccountId);
        return transactionsbyaccount;
    }

    public List<TransactionResponseDTO> getTransactionsByBill(UUID billId) {
        List<Transaction> transactions = transactionRepository.findByBill_Id(billId);
        return transactions.stream()
                .map(BillMapper::mapToDTO)
                .toList();
    }

}
