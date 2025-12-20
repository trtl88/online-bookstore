package com.orderprocessing.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TopCustomerReport {
    private Long userId;
    private String username;
    private String customerName;
    private String email;
    private Long totalOrders;
    private BigDecimal totalPurchaseAmount;
}
