package com.trtl88.backend.repositories;

import com.trtl88.backend.models.ReportDTOs.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. SALES (LAST MONTH)
    // Needs 3 tables: Orders (Date), OrderItems (Link), Book (Price)
    public Double getTotalSalesPreviousMonth() {
        String sql = """
            SELECT SUM(b.price * oi.quantity) 
            FROM orders o 
            JOIN order_items oi ON o.order_id = oi.order_id 
            JOIN book b ON oi.book_isbn = b.isbn 
            WHERE o.order_date >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)
        """;
        Double res = jdbcTemplate.queryForObject(sql, Double.class);
        return res != null ? res : 0.0;
    }

    // 2. SALES (SPECIFIC DAY)
    public Double getTotalSalesByDate(String date) {
        String sql = """
            SELECT SUM(b.price * oi.quantity) 
            FROM orders o 
            JOIN order_items oi ON o.order_id = oi.order_id 
            JOIN book b ON oi.book_isbn = b.isbn 
            WHERE o.order_date = ?
        """;
        Double res = jdbcTemplate.queryForObject(sql, Double.class, date);
        return res != null ? res : 0.0;
    }

    // 3. TOP 5 CUSTOMERS 
    public List<TopCustomer> getTop5Customers() {
        String sql = """
            SELECT o.username, SUM(b.price * oi.quantity) as total_spent
            FROM orders o
            JOIN order_items oi ON o.order_id = oi.order_id
            JOIN book b ON oi.book_isbn = b.isbn
            WHERE o.order_date >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
            GROUP BY o.username
            ORDER BY total_spent DESC
            LIMIT 5
        """;
        
        return jdbcTemplate.query(sql, (rs, n) -> 
            new TopCustomer(rs.getString("username"), rs.getDouble("total_spent"))
        );
    }

    // 4. TOP 10 BOOKS
    // Needs 3 tables to link Date (Orders) to Title (Book)
    public List<TopBook> getTop10Books() {
        String sql = """
            SELECT b.title, SUM(oi.quantity) as total_sold
            FROM book b 
            JOIN order_items oi ON b.isbn = oi.book_isbn 
            JOIN orders o ON oi.order_id = o.order_id
            WHERE o.order_date >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
            GROUP BY b.title
            ORDER BY total_sold DESC
            LIMIT 10
        """;

        return jdbcTemplate.query(sql, (rs, n) -> 
            new TopBook(rs.getString("title"), rs.getInt("total_sold"))
        );
    }

    // 5. RESTOCK COUNT
    public Integer getRestockCount(String isbn) {
        String sql = "SELECT COUNT(*) FROM publisher_orders WHERE book_isbn = ?";
        Integer res = jdbcTemplate.queryForObject(sql, Integer.class, isbn);
        return res != null ? res : 0;
    }
}