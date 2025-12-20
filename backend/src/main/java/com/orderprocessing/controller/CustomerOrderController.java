package com.orderprocessing.controller;

import com.orderprocessing.dto.CheckoutRequest;
import com.orderprocessing.model.CustomerOrder;
import com.orderprocessing.model.OrderItem;
import com.orderprocessing.service.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class CustomerOrderController {
    
    private final CustomerOrderService customerOrderService;
    
    @GetMapping
    public ResponseEntity<List<CustomerOrder>> getAllOrders() {
        return ResponseEntity.ok(customerOrderService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        return customerOrderService.findById(id)
            .map(order -> {
                List<OrderItem> items = customerOrderService.getOrderItems(id);
                Map<String, Object> response = new HashMap<>();
                response.put("orderId", order.getOrderId());
                response.put("userId", order.getUser().getUserId());
                response.put("customerName", order.getUser().getFullName());
                response.put("orderDate", order.getOrderDate());
                response.put("totalAmount", order.getTotalAmount());
                response.put("status", order.getStatus());
                response.put("shippingAddress", order.getShippingAddress());
                response.put("creditCardLastFour", order.getCreditCardLastFour());
                response.put("items", items.stream().map(item -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("bookId", item.getBook().getBookId());
                    itemMap.put("isbn", item.getBook().getIsbn());
                    itemMap.put("title", item.getBook().getTitle());
                    itemMap.put("quantity", item.getQuantity());
                    itemMap.put("unitPrice", item.getUnitPrice());
                    itemMap.put("subtotal", item.getSubtotal());
                    return itemMap;
                }).toList());
                return ResponseEntity.ok(response);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CustomerOrder>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(customerOrderService.findByUserId(userId));
    }
    
    @PostMapping("/checkout/{userId}")
    public ResponseEntity<?> checkout(@PathVariable Long userId, @RequestBody CheckoutRequest request) {
        try {
            CustomerOrder order = customerOrderService.checkout(userId, request);
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.getOrderId());
            response.put("totalAmount", order.getTotalAmount());
            response.put("status", order.getStatus());
            response.put("message", "Checkout successful! Thank you for your purchase.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            CustomerOrder.Status status = CustomerOrder.Status.valueOf(body.get("status"));
            CustomerOrder order = customerOrderService.updateStatus(id, status);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
