package com.trtl88.backend.services;

import com.trtl88.backend.models.ReportDTOs.*;
import com.trtl88.backend.repositories.ReportRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepo;

    public ReportService(ReportRepository reportRepo) {
        this.reportRepo = reportRepo;
    }

    // 1. Format: "$1,250.00"
    public String getLastMonthSales() {
        Double amount = reportRepo.getTotalSalesPreviousMonth();
        return String.format("$%,.2f", amount);
    }

    // 2. Format: "$500.50"
    public String getSalesByDate(String date) {
        Double amount = reportRepo.getTotalSalesByDate(date);
        return String.format("$%,.2f", amount);
    }

    // 3. Pass-through (See recommendation below about Lists)
    public List<TopCustomer> getTopCustomers() {
        return reportRepo.getTop5Customers();
    }

    public List<TopBook> getTopBooks() {
        return reportRepo.getTop10Books();
    }

    public Integer getRestockCount(String isbn) {
        return reportRepo.getRestockCount(isbn);
    }
}