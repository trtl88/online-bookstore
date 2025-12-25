package com.trtl88.backend.repositories;

import com.trtl88.backend.models.Order;
import com.trtl88.backend.models.OrderItemDTO;
import com.trtl88.backend.models.CartItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. CREATE ORDER (The Big Transaction)
    // We use @Transactional so if any step fails (e.g., negative stock), EVERYTHING
    // rolls back.
    @Transactional
    public void placeOrder(String username, List<CartItem> cartItems) {
        // Step A: Create the Order Record
        String createOrderSql = "INSERT INTO orders (username, order_date) VALUES (?, ?)";
        jdbcTemplate.update(createOrderSql, username, Date.valueOf(LocalDate.now()));

        // Step B: Get the new Order ID (Last Inserted ID)
        String getOrderIdSql = "SELECT LAST_INSERT_ID()";
        Long orderId = jdbcTemplate.queryForObject(getOrderIdSql, Long.class);

        // Step C: Loop through Cart Items to save them and update stock
        for (CartItem item : cartItems) {
            // 1. Add to Order Items
            String itemSql = "INSERT INTO order_items (order_id, book_isbn, quantity) VALUES (?, ?, ?)";
            jdbcTemplate.update(itemSql, orderId, item.getBookIsbn(), item.getQuantity());

            // 2. Deduct Stock from Book
            String stockSql = "UPDATE book SET stock_quantity = stock_quantity - ? WHERE isbn = ?";
            jdbcTemplate.update(stockSql, item.getQuantity(), item.getBookIsbn());
        }

        // Step D: Empty the Shopping Cart
        String clearCartSql = "DELETE FROM shopping_cart WHERE username = ?";
        jdbcTemplate.update(clearCartSql, username);
    }

    // 2. VIEW ORDER HISTORY (For Part 2, Section 5)
    public List<Order> getUserOrders(String username) {
        String sql = "SELECT * FROM orders WHERE username = ? ORDER BY order_date DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Order order = new Order();
            order.setOrderId(rs.getLong("order_id"));
            order.setUsername(rs.getString("username"));
            order.setOrderDate(rs.getDate("order_date"));
            return order;
        });
    }
    // ... inside OrderRepository class ...

    // 3. GET ITEMS FOR A SPECIFIC ORDER
    public List<OrderItemDTO> getOrderItems(Long orderId) {
        String sql = """
                    SELECT oi.book_isbn, b.title, oi.quantity, b.price
                    FROM order_items oi
                    JOIN book b ON oi.book_isbn = b.isbn
                    WHERE oi.order_id = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new OrderItemDTO(
                rs.getString("book_isbn"),
                rs.getString("title"),
                rs.getInt("quantity"),
                rs.getDouble("price")),
                orderId);
    }
}