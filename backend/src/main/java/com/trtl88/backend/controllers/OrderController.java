package com.trtl88.backend.controllers;

import com.trtl88.backend.models.Order;
import com.trtl88.backend.models.OrderItemDTO;
import com.trtl88.backend.services.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 1. CHECKOUT (Buy Items)
    // POST /api/orders/checkout?username=john&cc=1234567812345678&expiry=12/25
    @PostMapping("/checkout")
    public String checkout(@RequestParam String username,
            @RequestParam String cc,
            @RequestParam String expiry) {
        return orderService.checkout(username, cc, expiry);
    }

    // 2. HISTORY (View Past Orders)
    // GET /api/orders/history/john
    @GetMapping("/history/{username}")
    public List<Order> getOrderHistory(@PathVariable String username) {
        return orderService.getUserOrders(username);
    }
    // ... inside OrderController class ...

    // 3. GET ORDER DETAILS
    // Usage: GET /api/orders/details/5 (where 5 is the order ID)
    @GetMapping("/details/{orderId}")
    public List<OrderItemDTO> getOrderDetails(@PathVariable Long orderId) {
        return orderService.getOrderDetails(orderId);
    }
}