package com.example.billservice.dto;

import java.time.LocalDate;
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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class LineResponseDTO {
    private String itemType;
    private String description;
    private Integer quantity;
    private Double unitLineAmount;
    private Double totalLineAmount;
}