package com.orderprocessing.dto;

import lombok.Data;

@Data
public class CheckoutRequest {
    private String creditCardNumber;
    private String shippingAddress;
}
