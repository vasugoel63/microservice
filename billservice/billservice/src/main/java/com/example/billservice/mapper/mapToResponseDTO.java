package com.example.billservice.mapper;
import java.util.List;

import javax.sound.sampled.Line;

import com.example.billservice.dto.BillResponseDTO;
import com.example.billservice.dto.LineResponseDTO;
import com.example.billservice.entities.Bill;

public BillResponseDTO mapToResponseDTO(Bill bill) {
    List<LineResponseDTO> itemDTOs = bill.getItems().stream().map(item -> {
        LineResponseDTO lineResponseDTO = new LineResponseDTO();
        lineResponseDTO.setItemType(item.getItemType());
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
    return billResponseDTO;
}
