package com.orderprocessing.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingZipCode;
    private String shippingCountry;
    
    @NotNull(message = "Order items are required")
    @Size(min = 1, message = "At least one order item is required")
    @Valid
    private List<OrderItemRequest> orderItems;
}
