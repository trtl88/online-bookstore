package com.trtl88.backend;

import java.util.List;

public abstract class User {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String address;
    private String phoneNumber;
    private String shippingAddress;
    
    public User(String firstName, String lastName, String username, String password, String email, String address, String phoneNumber, String shippingAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public abstract Book searchByISBN(String isbn);
    public abstract List<Book> searchByTitle(String title);
    public abstract List<Book> searchByCategory(String category);
    public abstract List<Book> searchByAuthor(String author);
    public abstract List<Book> searchByPublisher(String publisher);
}