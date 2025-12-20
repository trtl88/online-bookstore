package com.orderprocessing.dto;

import lombok.Data;

@Data
public class InventoryRequest {
    private Long productId;
    private Integer quantity;
    private Integer reorderLevel;
}
