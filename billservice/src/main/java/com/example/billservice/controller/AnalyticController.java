package com.example.billservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.billservice.dto.RevenueDetailsDTO;
import com.example.billservice.services.AnalyticsService;

@RestController
@RequestMapping("/api/bill")
public class AnalyticController {
    private final AnalyticsService analyticsService;

    public AnalyticController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/count/transactioncount")
    public RevenueDetailsDTO getRevenueDetails() {
        return analyticsService.getRevenuDetails();
    }

    @GetMapping("/hello")
    public String hello() {
        return "HELLO";
    }
}
