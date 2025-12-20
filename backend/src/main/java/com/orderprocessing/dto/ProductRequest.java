package com.orderprocessing.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String productName;
    private String description;
    private String category;
    private Double unitPrice;
    private Long supplierId;
}
