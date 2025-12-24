package com.trtl88.backend.models;
import java.util.List;
import java.time.LocalDate;

public class Order {
    private int orderId;
    private Publisher publisher;
    private List<Book> books;
    private LocalDate orderDate;

    public Order(int orderId, Publisher publisher,
                 List<Book> books, LocalDate orderDate) {
        this.orderId = orderId;
        this.publisher = publisher;
        this.books = books;
        this.orderDate = orderDate;
    }

    // Getters
    public int getOrderId() {
        return orderId;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public List<Book> getBooks() {
        return books;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    // Setters
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
}
