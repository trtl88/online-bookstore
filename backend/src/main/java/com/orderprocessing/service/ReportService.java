package com.orderprocessing.service;

import com.orderprocessing.dto.*;
import com.orderprocessing.model.User;
import com.orderprocessing.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    
    private final CustomerOrderRepository customerOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BookOrderRepository bookOrderRepository;
    private final UserRepository userRepository;
    
    /**
     * Get total sales for the previous month
     */
    public SalesReport getSalesPreviousMonth() {
        LocalDateTime startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        
        return getSalesForPeriod(startDate, endDate, "Previous Month Sales");
    }
    
    /**
     * Get total sales for a specific day
     */
    public SalesReport getSalesForDate(LocalDate date) {
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.plusDays(1).atStartOfDay();
        
        return getSalesForPeriod(startDate, endDate, "Sales for " + date.toString());
    }
    
    private SalesReport getSalesForPeriod(LocalDateTime startDate, LocalDateTime endDate, String reportName) {
        Long orderCount = customerOrderRepository.countOrdersBetweenDates(startDate, endDate);
        BigDecimal totalAmount = customerOrderRepository.sumTotalAmountBetweenDates(startDate, endDate);
        Long booksSold = orderItemRepository.sumQuantityBetweenDates(startDate, endDate);
        
        SalesReport report = new SalesReport();
        report.setReportName(reportName);
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setTotalOrders(orderCount != null ? orderCount : 0L);
        report.setTotalSalesAmount(totalAmount != null ? totalAmount : BigDecimal.ZERO);
        report.setTotalBooksSold(booksSold != null ? booksSold : 0L);
        
        return report;
    }
    
    /**
     * Get top 5 customers in the last 3 months
     */
    public List<TopCustomerReport> getTop5Customers() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        List<TopCustomerReport> result = new ArrayList<>();
        
        List<User> customers = userRepository.findByRole(User.Role.CUSTOMER);
        
        for (User customer : customers) {
            List<com.orderprocessing.model.CustomerOrder> orders = 
                customerOrderRepository.findByUser_UserId(customer.getUserId());
            
            BigDecimal totalPurchase = orders.stream()
                .filter(o -> o.getOrderDate().isAfter(threeMonthsAgo))
                .map(com.orderprocessing.model.CustomerOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            long orderCount = orders.stream()
                .filter(o -> o.getOrderDate().isAfter(threeMonthsAgo))
                .count();
            
            if (orderCount > 0) {
                TopCustomerReport report = new TopCustomerReport();
                report.setUserId(customer.getUserId());
                report.setUsername(customer.getUsername());
                report.setCustomerName(customer.getFullName());
                report.setEmail(customer.getEmail());
                report.setTotalOrders(orderCount);
                report.setTotalPurchaseAmount(totalPurchase);
                result.add(report);
            }
        }
        
        // Sort by total purchase amount and limit to 5
        result.sort((a, b) -> b.getTotalPurchaseAmount().compareTo(a.getTotalPurchaseAmount()));
        return result.size() > 5 ? result.subList(0, 5) : result;
    }
    
    /**
     * Get top 10 selling books in the last 3 months
     */
    public List<TopBookReport> getTop10Books() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        List<Object[]> results = orderItemRepository.findTopSellingBooks(threeMonthsAgo);
        
        List<TopBookReport> reports = new ArrayList<>();
        int count = 0;
        
        for (Object[] row : results) {
            if (count >= 10) break;
            
            TopBookReport report = new TopBookReport();
            report.setBookId((Long) row[0]);
            report.setTitle((String) row[1]);
            report.setTotalCopiesSold(((Number) row[2]).longValue());
            reports.add(report);
            count++;
        }
        
        return reports;
    }
    
    /**
     * Get order count for a specific book (replenishment orders)
     */
    public BookOrderCountReport getBookOrderCount(Long bookId) {
        Long orderCount = bookOrderRepository.countOrdersByBookId(bookId);
        Long totalQuantity = bookOrderRepository.sumQuantityByBookId(bookId);
        
        BookOrderCountReport report = new BookOrderCountReport();
        report.setBookId(bookId);
        report.setTimesOrdered(orderCount != null ? orderCount : 0L);
        report.setTotalQuantityOrdered(totalQuantity != null ? totalQuantity : 0L);
        
        return report;
    }
}
