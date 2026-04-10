package com.example.billservice.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SecondaryRow;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BillRequestDTO {
    @NotNull
    private String patientId;

    private String status;

    @NotNull
    private List<LineRequestDTO> lineItems;

    public String getItemType;
}
