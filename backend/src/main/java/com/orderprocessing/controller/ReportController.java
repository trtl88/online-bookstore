package com.orderprocessing.controller;

import com.orderprocessing.dto.*;
import com.orderprocessing.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {
    
    private final ReportService reportService;
    
    /**
     * Get total sales for the previous month
     */
    @GetMapping("/sales/previous-month")
    public ResponseEntity<SalesReport> getSalesPreviousMonth() {
        return ResponseEntity.ok(reportService.getSalesPreviousMonth());
    }
    
    /**
     * Get total sales for a specific day
     */
    @GetMapping("/sales/date")
    public ResponseEntity<SalesReport> getSalesForDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportService.getSalesForDate(date));
    }
    
    /**
     * Get top 5 customers in the last 3 months
     */
    @GetMapping("/top-customers")
    public ResponseEntity<List<TopCustomerReport>> getTop5Customers() {
        return ResponseEntity.ok(reportService.getTop5Customers());
    }
    
    /**
     * Get top 10 selling books in the last 3 months
     */
    @GetMapping("/top-books")
    public ResponseEntity<List<TopBookReport>> getTop10Books() {
        return ResponseEntity.ok(reportService.getTop10Books());
    }
    
    /**
     * Get order count for a specific book (replenishment orders)
     */
    @GetMapping("/book-orders/{bookId}")
    public ResponseEntity<BookOrderCountReport> getBookOrderCount(@PathVariable Long bookId) {
        return ResponseEntity.ok(reportService.getBookOrderCount(bookId));
    }
}
