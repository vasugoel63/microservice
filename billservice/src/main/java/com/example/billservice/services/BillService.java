package com.example.billservice.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.example.billservice.dto.BillRequestDTO;
import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.dto.BillingAccountResponse;
import com.example.billservice.dto.LineRequestDTO;
import com.example.billservice.dto.TransactionDTO;
import com.example.billservice.entities.Bill;
import com.example.billservice.entities.BillStatus;
import com.example.billservice.entities.ItemType;
import com.example.billservice.entities.LineItem;
import com.example.billservice.entities.PaymentMethod;
import com.example.billservice.entities.Transaction;
import com.example.billservice.entities.TransactionStatus;
import com.example.billservice.event.BillUpdateEvent;
import com.example.billservice.kafkaProducer.BillKafkaProducer;
import com.example.billservice.repository.BillRepository;
import com.example.billservice.repository.LineItemRepository;
import com.example.billservice.repository.TransactionRepository;
import com.example.billservice.mapper.BillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.billservice.services.EmailService;

@Service
public class BillService {
    private BillRepository billRepository;
    private LineItemRepository lineItemRepository;
    private TransactionRepository transactionRepository;
    private TransactionService transactionService;

    @Autowired
    private EmailService emailService;

    public BillService(BillRepository billRepository, LineItemRepository lineItemRepository,
            TransactionRepository transactionRepository,
            TransactionService transactionService) {
        this.billRepository = billRepository;
        this.lineItemRepository = lineItemRepository;

        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    public BillResponseDTO createBill(BillRequestDTO request) {
        Bill bill = new Bill();
        bill.setPatientId(request.getPatientId());
        // bill.setPurchaseDateTime(request.getPurchaseDate());
        bill.setStatus(BillStatus.PENDING);
        List<LineItem> lineItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (LineRequestDTO itemDTO : request.getLineItems()) {
            LineItem lineitem = new LineItem();
            lineitem.setItemType(ItemType.valueOf(itemDTO.getItemType()));
            lineitem.setDescription(itemDTO.getDescription());
            lineitem.setQuantity(itemDTO.getQuantity());
            lineitem.setUnitLineAmount(itemDTO.getUnitLineAmount());
            double totalLineAmount = itemDTO.getQuantity() * itemDTO.getUnitLineAmount();
            lineitem.setTotalLineAmount(totalLineAmount);
            lineitem.setBill(bill);
            lineItems.add(lineitem);
            totalAmount += totalLineAmount;
        }
        String billaqid = generateBillAQId();
        bill.setBillAQId(billaqid);
        bill.setTotalAmount(totalAmount);
        bill.setItems(lineItems);
        bill.setPurchaseDateTime(LocalDateTime.now());
        Bill savedBill = billRepository.save(bill);
        String body = """
        Hi,

        Your bill has been created successfully.

        Bill AQ ID: %s
        Total Amount: %s
        Purchase Date: %s

        Thank you.
        """
        .formatted(
                savedBill.getBillAQId(),
                savedBill.getTotalAmount(),
                savedBill.getPurchaseDateTime()
        );
        emailService.sendEmail("vasugoel4308@gmail.com",  "Bill created successfully",
                    body);
        // update billing account and balance

        // BillingAccount
        // TransactionService call
        // Transaction transaction = new Transaction();
        // transaction.setAmount(BigDecimal.valueOf(totalAmount));
        // transaction.setBillAQId(billaqid);
        // transaction.setPaymentMethod(PaymentMethod.UPI);
        // transaction.setStatus(TransactionStatus.SUCCESS);
        // TransactionDTO txnDTO = new TransactionDTO();

        return BillMapper.mapToResponseDTO(savedBill);

    }

    public List<Bill> getBills() {
        List<Bill> bills = billRepository.findAll(Sort.by(Sort.Direction.DESC, "purchaseDateTime"));
        return bills;
    }

    public List<Bill> getBillByPatient(String patientId) {
        return billRepository.findByPatientId(
                patientId,
                Sort.by(Sort.Direction.DESC, "purchaseDateTime"));
    }

    public Bill updateStatus(UUID billId, String status) {
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new RuntimeException("Bill not found"));
        if (bill.getStatus() == BillStatus.PAID) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bill already paid");
        }
        bill.setStatus(BillStatus.PAID);
        bill.setPaidDateTime(LocalDateTime.now());
        return billRepository.save(bill);
    }

    public BillResponseDTO getBill(UUID billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill not found"));

        return BillMapper.mapToResponseDTO(bill);
    }

    public void deleteBill(UUID billId) {
        if (!billRepository.existsById(billId)) {
            throw new RuntimeException("Bill not found");
        }
        billRepository.deleteById(billId);
    }

    public String generateBillAQId() {
        long count = billRepository.count() + 1;
        return String.format("AQ-%04d", count);
    }
}
