package com.trtl88.backend.repositories;

import com.trtl88.backend.models.PublisherOrder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PublisherOrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public PublisherOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Mapper to convert SQL rows into Java Objects
    @NonNull
    private final RowMapper<PublisherOrder> orderMapper = (rs, rowNum) -> {
        PublisherOrder order = new PublisherOrder();
        order.setOrderId(rs.getLong("order_id"));
        order.setBookIsbn(rs.getString("book_isbn"));
        order.setQuantity(rs.getInt("quantity"));
        order.setStatus(rs.getString("status"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        return order;
    };

    // SQL 1: Find all orders that are waiting for confirmation
    public List<PublisherOrder> findPendingOrders() {
        String sql = "SELECT * FROM publisher_orders WHERE status = 'PENDING'";
        return jdbcTemplate.query(sql, orderMapper);
    }

    // SQL 2: Find a single order by ID
    public PublisherOrder findById(Long id) {
        String sql = "SELECT * FROM publisher_orders WHERE order_id = ?";
        List<PublisherOrder> orders = jdbcTemplate.query(sql, orderMapper, id);
        return orders.isEmpty() ? null : orders.get(0);
    }

    // SQL 3: Update the status of an order
    public void updateStatus(Long orderId, String newStatus) {
        String sql = "UPDATE publisher_orders SET status = ? WHERE order_id = ?";
        jdbcTemplate.update(sql, newStatus, orderId);
    }
}