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

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class LineRequestDTO {
    @NotNull
    private String itemType;

    @NotNull
    private String description;

    @NotNull
    private Integer quantity;

    @NotNull
    private Double unitLineAmount;

}