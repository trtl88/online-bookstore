package com.trtl88.backend;
import java.util.List;

public class Book {
    private String isbn;
    private String title;
    private List<Author> authors;
    private Publisher publisher;
    private int publicationYear;
    private double price;
    private Category category;
    private int stockQuantity;


    public Book(String isbn, String title, List<Author> authors,
                Publisher publisher, int publicationYear,
                double price, Category category, int stockQuantity) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
    }

    // Getters
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public List<Author> getAuthors() {
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

    public Category getCategory() {
        return category;
    }

    // Setters
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(List<Author> authors) {
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

    public void setCategory(Category category) {
        this.category = category;
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
}
