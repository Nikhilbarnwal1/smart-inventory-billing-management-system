package com.smartinventory.inventory_management.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartinventory.inventory_management.dto.SalesReportSummary;
import com.smartinventory.inventory_management.entity.Sale;
import com.smartinventory.inventory_management.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/sales")
    public List<Sale> getSalesReport(
            @RequestParam String from,
            @RequestParam String to) {

        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);

        LocalDateTime startDateTime
                = fromDate.atStartOfDay();

        LocalDateTime endDateTime
                = toDate.atTime(LocalTime.MAX);

        return reportService.getSalesBetweenDates(
                startDateTime,
                endDateTime
        );
    }

    // Sales report summary
    @GetMapping("/sales/summary")
    public SalesReportSummary getSalesSummary(
            @RequestParam String from,
            @RequestParam String to) {

        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);

        LocalDateTime startDateTime
                = fromDate.atStartOfDay();

        LocalDateTime endDateTime
                = toDate.atTime(LocalTime.MAX);

        return reportService.getSalesSummary(
                startDateTime,
                endDateTime
        );
    }
}
