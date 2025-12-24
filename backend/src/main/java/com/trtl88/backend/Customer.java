package com.trtl88.backend;

import java.util.List;

public class Customer extends User {

    public Customer(String firstName, String lastName, String username, String password, String email, String address, String phoneNumber, String shippingAddress) {
        super(firstName, lastName, username, password, email, address, phoneNumber, shippingAddress);
    }

    @Override
    public Book searchByISBN(String isbn) {
        // Implementation for searching a book by ISBN
        return null;
    }
    @Override
    public List<Book> searchByTitle(String title) {
        // Implementation for searching books by title
        return null; 
    }
    @Override
    public List<Book> searchByCategory(String category) {
        // Implementation for searching books by category
        return null; 
    }
    @Override
    public List<Book> searchByAuthor(String author) {
        // Implementation for searching books by author
        return null; 
    }
    @Override
    public List<Book> searchByPublisher(String publisher) {
        // Implementation for searching books by publisher
        return null; 
    }
}