package com.example.billservice.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RevenueDetailsDTO {

    public RevenueDetailsDTO(int successCount, int failedCount, int pendingCount, BigDecimal totalRevenue,
            int todayBillsCount, int pendingBcount) {
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.pendingCount = pendingCount;
        this.todayBillsCount = todayBillsCount;
        this.totalRevenue = totalRevenue;
        this.pendingbCount = pendingBcount;
    }

    private int successCount;
    private int failedCount;
    private int pendingCount;
    private int todayBillsCount;
    private BigDecimal totalRevenue;
    private int pendingbCount;

}