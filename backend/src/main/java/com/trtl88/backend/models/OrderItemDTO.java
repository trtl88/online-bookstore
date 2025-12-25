package com.trtl88.backend.models;

public class OrderItemDTO {
    private String bookIsbn;
    private String title;
    private int quantity;
    private double priceAtPurchase; // Or current price, depending on requirements

    public OrderItemDTO() {
    }

    public OrderItemDTO(String bookIsbn, String title, int quantity, double price) {
        this.bookIsbn = bookIsbn;
        this.title = title;
        this.quantity = quantity;
        this.priceAtPurchase = price;
    }

    // Getters and Setters
    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }
}