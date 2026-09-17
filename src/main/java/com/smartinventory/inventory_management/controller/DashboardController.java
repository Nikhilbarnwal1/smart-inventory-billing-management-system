package com.smartinventory.inventory_management.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartinventory.inventory_management.service.DashboardSummary;
import com.smartinventory.inventory_management.service.DashboardSummaryService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardSummaryService dashboardSummaryService;

    public DashboardController(
            DashboardSummaryService dashboardSummaryService) {

        this.dashboardSummaryService = dashboardSummaryService;
    }

    @GetMapping("/summary")
    public DashboardSummary getDashboardSummary() {
        return dashboardSummaryService.getDashboardSummary();
    }
}
