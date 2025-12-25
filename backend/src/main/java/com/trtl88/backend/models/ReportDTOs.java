package com.trtl88.backend.models;

public class ReportDTOs {

    // Holds result for "Top 5 Customers"
    public static class TopCustomer {
        public String username;
        public double totalSpent;

        public TopCustomer(String u, double t) {
            this.username = u;
            this.totalSpent = t;
        }
    }

    // Holds result for "Top 10 Books"
    public static class TopBook {
        public String title;
        public int totalCopiesSold;

        public TopBook(String t, int c) {
            this.title = t;
            this.totalCopiesSold = c;
        }
    }
}