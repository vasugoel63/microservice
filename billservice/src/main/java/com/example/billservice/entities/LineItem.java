package com.example.billservice.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // bill id
    private UUID billId;

    @NotNull
    private String itemType;

    @NotNull
    private String description;

    private Integer quantity;

    private Double unitLineAmount;

    private Double totalLineAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    private Bill bill;

}
