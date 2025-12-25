package com.trtl88.backend.models;

import java.sql.Date; // Using java.sql.Date matches JDBC easier than LocalDate

public class Order {
    private Long orderId; // Changed to Long (Database IDs are usually Long)
    private String username; // CHANGED: Links to the User who bought the books
    private Date orderDate; // Matches the database 'DATE' column

    // Empty Constructor (Required for RowMapper)
    public Order() {
    }

    // Full Constructor
    public Order(Long orderId, String username, Date orderDate) {
        this.orderId = orderId;
        this.username = username;
        this.orderDate = orderDate;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}