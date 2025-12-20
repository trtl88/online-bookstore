package com.orderprocessing.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SalesReport {
    private String reportName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long totalOrders;
    private BigDecimal totalSalesAmount;
    private Long totalBooksSold;
}
