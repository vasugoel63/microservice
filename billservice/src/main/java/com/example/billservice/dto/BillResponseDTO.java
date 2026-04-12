package com.example.billservice.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SecondaryRow;

import jakarta.validation.constraints.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter

public class BillResponseDTO {
    private String billId;
    private String billAQId;
    private String patientId;
    private String status;
    private Double totalAmount;
    private List<LineResponseDTO> lineItems;

}