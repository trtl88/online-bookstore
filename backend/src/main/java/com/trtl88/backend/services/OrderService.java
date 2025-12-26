package com.trtl88.backend.services;

import com.trtl88.backend.models.CartItem;
import com.trtl88.backend.models.Order;
import com.trtl88.backend.models.OrderItemDTO;
import com.trtl88.backend.repositories.CartRepository;
import com.trtl88.backend.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;

    public OrderService(OrderRepository orderRepo, CartRepository cartRepo) {
        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
    }

    public String checkout(String username, String creditCardNumber, String expiryDate) {
        // 1. Validate Credit Card (Simple Mock Logic)
        // You can make this stricter if you want (e.g., check length)
        if (creditCardNumber == null || creditCardNumber.length() < 16) {
            return "Error: Invalid Credit Card Number";
        }

        // 2. Get Cart Items
        List<CartItem> cartItems = cartRepo.getCartItems(username);
        if (cartItems.isEmpty()) {
            return "Error: Cart is empty.";
        }

        // 3. Process the Order (Transaction)
        try {
            orderRepo.placeOrder(username, cartItems);
            return "Success: Order placed successfully!";
        } catch (Exception e) {
            // This catches the Trigger Error if stock goes below 0
            return "Error: Transaction failed. " + e.getMessage();
        }
    }

    public List<Order> getUserOrders(String username) {
        return orderRepo.getUserOrders(username);
    }
    // ... inside OrderService class ...

    public List<OrderItemDTO> getOrderDetails(Long orderId) {
        return orderRepo.getOrderItems(orderId);
    }
}