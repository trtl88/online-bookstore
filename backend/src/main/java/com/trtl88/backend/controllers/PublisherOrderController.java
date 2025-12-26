package com.trtl88.backend.controllers;

import com.trtl88.backend.models.PublisherOrder;
import com.trtl88.backend.repositories.PublisherOrderRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager/orders")
@CrossOrigin(origins = "*")
public class PublisherOrderController {

    private final PublisherOrderRepository orderRepo;
    private final JdbcTemplate jdbcTemplate; // Using this to directly update book stock

    public PublisherOrderController(PublisherOrderRepository orderRepo, JdbcTemplate jdbcTemplate) {
        this.orderRepo = orderRepo;
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. View Pending Orders
    @GetMapping("/pending")
    public List<PublisherOrder> getPendingOrders() {
        return orderRepo.findPendingOrders();
    }

    // 2. Confirm Order
    @PostMapping("/confirm/{orderId}")
    public String confirmOrder(@PathVariable Long orderId) {
        // Step A: Find the order
        PublisherOrder order = orderRepo.findById(orderId);
        if (order == null)
            return "Error: Order not found.";
        if ("CONFIRMED".equals(order.getStatus()))
            return "Error: Order already confirmed.";

        // Step B: Update the Book Stock (Manual SQL Query)
        // Logic: Add the order quantity to the existing stock
        String updateBookSql = "UPDATE book SET stock_quantity = stock_quantity + ? WHERE isbn = ?";
        int rowsUpdated = jdbcTemplate.update(updateBookSql, order.getQuantity(), order.getBookIsbn());

        if (rowsUpdated == 0) {
            return "Error: Could not update book stock. ISBN might be missing.";
        }

        // Step C: Mark order as confirmed
        orderRepo.updateStatus(orderId, "CONFIRMED");

        return "Success: Stock updated by " + order.getQuantity() + " units.";
    }
}