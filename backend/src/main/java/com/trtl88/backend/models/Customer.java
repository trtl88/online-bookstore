package com.trtl88.backend.models;

import java.util.List;

public class Customer extends User {

    public Customer(String firstName, String lastName, String username, String password, String email, String address, String phoneNumber, String shippingAddress) {
        super(firstName, lastName, username, password, email, address, phoneNumber, shippingAddress);
    }


}