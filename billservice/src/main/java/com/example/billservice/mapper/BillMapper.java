package com.example.billservice.mapper;

import java.util.List;
import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.dto.LineResponseDTO;
import com.example.billservice.dto.TransactionResponseDTO;
import com.example.billservice.entities.Bill;
import com.example.billservice.entities.Transaction;

public class BillMapper {

    public static BillResponseDTO mapToResponseDTO(Bill bill) {

        List<LineResponseDTO> itemDTOs = bill.getItems().stream().map(item -> {
            LineResponseDTO lineResponseDTO = new LineResponseDTO();
            lineResponseDTO.setItemType(item.getItemType().name());
            lineResponseDTO.setDescription(item.getDescription());
            lineResponseDTO.setQuantity(item.getQuantity());
            lineResponseDTO.setUnitLineAmount(item.getUnitLineAmount());
            lineResponseDTO.setTotalLineAmount(item.getTotalLineAmount());
            return lineResponseDTO;
        }).toList();

        BillResponseDTO billResponseDTO = new BillResponseDTO();
        billResponseDTO.setBillId(bill.getId().toString());
        billResponseDTO.setPatientId(bill.getPatientId());
        billResponseDTO.setStatus(bill.getStatus());
        billResponseDTO.setTotalAmount(bill.getTotalAmount());
        billResponseDTO.setLineItems(itemDTOs);
        billResponseDTO.setBillAQId(bill.getBillAQId());
        billResponseDTO.setPurchaseDate(bill.getPurchaseDateTime());
        billResponseDTO.setPaidDate(bill.getPaidDateTime());
        return billResponseDTO;
    }

    public static TransactionResponseDTO mapToDTO(Transaction txn) {
        TransactionResponseDTO dto = new TransactionResponseDTO();

        dto.setId(txn.getId());
        dto.setAmount(txn.getAmount());
        dto.setBillingAccountId(txn.getBillingAccountId());
        dto.setStatus(txn.getStatus().name());
        dto.setPaymentMethod(txn.getPaymentMethod().name());
        dto.setTransactionRefId(txn.getTransactionRefId());
        dto.setRazorpay_order_id(txn.getRazorpay_order_id());
        dto.setRazorpay_payment_id(txn.getRazorpay_payment_id());
        dto.setFailureReason(txn.getFailureReason());
        dto.setCreatedAt(txn.getCreatedAt());

        // 🔥 important part
        if (txn.getBill() != null) {
            dto.setBillId(txn.getBill().getId()); // UUID
            dto.setBillAQId(txn.getBill().getBillAQId()); // business ID
        }

        return dto;
    }
}