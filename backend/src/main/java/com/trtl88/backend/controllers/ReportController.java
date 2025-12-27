package com.trtl88.backend.controllers;

import com.trtl88.backend.models.ReportDTOs.*;
import com.trtl88.backend.services.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Changed return type to String
    @GetMapping("/sales/last-month")
    public String getLastMonthSales() {
        return reportService.getLastMonthSales();
    }

    // Changed return type to String
    @GetMapping("/sales/date")
    public String getSalesByDate(@RequestParam String date) {
        return reportService.getSalesByDate(date);
    }

    // Lists remain the same
    @GetMapping("/top-customers")
    public List<TopCustomer> getTopCustomers() {
        return reportService.getTopCustomers();
    }

    @GetMapping("/top-books")
    public List<TopBook> getTopBooks() {
        return reportService.getTopBooks();
    }

    @GetMapping("/restock-count")
    public Integer getRestockCount(@RequestParam String isbn) {
        return reportService.getRestockCount(isbn);
    }
}