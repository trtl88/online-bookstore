package com.orderprocessing.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TopBookReport {
    private Long bookId;
    private String isbn;
    private String title;
    private String category;
    private Long totalCopiesSold;
    private BigDecimal totalRevenue;
}
