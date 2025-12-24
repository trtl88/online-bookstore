package com.trtl88.backend.models;
public class Publisher {
    private long publisherId;
    private String name;
    private String address;
    private String phoneNumber;

    public Publisher(String name, String address, String phoneNumber, long publisherId) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.publisherId = publisherId;
    }
    public Publisher() {
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public long getPublisherId() {
        return publisherId;
    }
    public void setPublisherId(long publisherId) {
        this.publisherId = publisherId;
    }
}
