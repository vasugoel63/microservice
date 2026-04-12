package com.example.billservice.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.billservice.dto.BillRequestDTO;
import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.dto.LineRequestDTO;
import com.example.billservice.entities.Bill;
import com.example.billservice.entities.LineItem;
import com.example.billservice.event.BillUpdateEvent;
import com.example.billservice.kafkaProducer.BillKafkaProducer;
import com.example.billservice.repository.BillRepository;
import com.example.billservice.repository.LineItemRepository;
import com.example.billservice.mapper.BillMapper;

@Service
public class BillService {
    private BillRepository billRepository;
    private LineItemRepository lineItemRepository;
    private final BillKafkaProducer billKafkaProducer;

    public BillService(BillRepository billRepository, LineItemRepository lineItemRepository,
            BillKafkaProducer billKafkaProducer) {
        this.billRepository = billRepository;
        this.lineItemRepository = lineItemRepository;
        this.billKafkaProducer = billKafkaProducer;
    }

    public BillResponseDTO createBill(BillRequestDTO request) {
        Bill bill = new Bill();
        bill.setPatientId(request.getPatientId());
        bill.setPurchaseDate(LocalDate.now());
        bill.setStatus("PAID");
        List<LineItem> lineItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (LineRequestDTO itemDTO : request.getLineItems()) {
            LineItem lineitem = new LineItem();
            lineitem.setItemType(itemDTO.getItemType());
            lineitem.setDescription(itemDTO.getDescription());
            lineitem.setQuantity(itemDTO.getQuantity());
            lineitem.setUnitLineAmount(itemDTO.getUnitLineAmount());
            double totalLineAmount = itemDTO.getQuantity() * itemDTO.getUnitLineAmount();
            lineitem.setTotalLineAmount(totalLineAmount);
            lineitem.setBill(bill);
            lineItems.add(lineitem);
            totalAmount += totalLineAmount;
        }
        bill.setBillAQId(generateBillAQId());
        bill.setTotalAmount(totalAmount);
        bill.setItems(lineItems);
        Bill savedBill = billRepository.save(bill);
        // update billing account and balance
        // BillingAccount

        BillUpdateEvent event = new BillUpdateEvent();
        event.setPatientId(savedBill.getPatientId());
        event.setAmount(savedBill.getTotalAmount());
        billKafkaProducer.sendBillCreatedEvent(event);
        return BillMapper.mapToResponseDTO(savedBill);

    }

    public List<Bill> getBillByPatient(String patientId) {
        return billRepository.findByPatientId(patientId);
    }

    public Bill updateBillStatus(UUID billId, String status) {
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new RuntimeException("Bill not found"));
        bill.setStatus(status);
        return billRepository.save(bill);
    }

    public Bill getBill(UUID billId) {
        return billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
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
