package com.trtl88.backend.models;

import java.util.Arrays;
import java.util.List;

public class Book {
    private String isbn;
    private String title;
    private List<String> authors;
    private Publisher publisher;
    private int publicationYear;
    private double price;
    private String category;
    private int stockQuantity;
    private int threshold;

    public Book(String isbn, String title, List<String> authors,
            Publisher publisher, int publicationYear,
            double price, String category, int stockQuantity, int threshold) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.threshold = threshold;
    }

    public Book() {
    }

    // Getters
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    // Setters
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(String category) {

        List<String> allowedCategories = Arrays.asList(
                "Science", "Art", "Religion", "History", "Geography");

        if (allowedCategories.contains(category)) {
            this.category = category;
        } else {
            // This prevents "bad data" from ever reaching your Friend's database
            throw new IllegalArgumentException("Invalid category. Must be one of: " + allowedCategories);
        }
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public boolean isAvailable() {
        return stockQuantity > 0;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void setPublisherName(String publisherName) {
        if (this.publisher == null) {
            this.publisher = new Publisher();
        }
        this.publisher.setName(publisherName);
    }

    public void setAuthorNames(List<String> authors) {
        if (authors == null || authors.isEmpty())
            return;
        this.authors = authors;
    }

    public int getPublisherId() {
        // This prevents the app from crashing if no publisher is assigned yet
        return (this.publisher != null) ? this.publisher.getPublisherId() : 0;
    }

    public void setPublisherId(int publisherId) {
        if (this.publisher == null) {
            this.publisher = new Publisher();
        }
        this.publisher.setPublisherId(publisherId);
    }

}
