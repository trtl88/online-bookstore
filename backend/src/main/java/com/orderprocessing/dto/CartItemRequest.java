package com.orderprocessing.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long bookId;
    private Integer quantity;
}
