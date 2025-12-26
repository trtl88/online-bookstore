package com.trtl88.backend.repositories;

import com.trtl88.backend.models.CartItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartRepository {

    private final JdbcTemplate jdbcTemplate;

    public CartRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. ADD TO CART (Smart Insert)
    public void addToCart(String username, String isbn, int quantity) {
        // "ON DUPLICATE KEY UPDATE" means:
        // If this user already has this book in cart, just add to the existing
        // quantity.
        String sql = """
                    INSERT INTO shopping_cart (username, book_isbn, quantity)
                    VALUES (?, ?, ?)
                    ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)
                """;
        jdbcTemplate.update(sql, username, isbn, quantity);
    }

    // 2. VIEW CART
    public List<CartItem> getCartItems(String username) {
        String sql = """
                    SELECT sc.username, sc.book_isbn, b.title, b.price, sc.quantity,
                           (b.price * sc.quantity) as total_item_price
                    FROM shopping_cart sc
                    JOIN book b ON sc.book_isbn = b.isbn
                    WHERE sc.username = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CartItem item = new CartItem();
            item.setUsername(rs.getString("username"));
            item.setBookIsbn(rs.getString("book_isbn"));
            item.setTitle(rs.getString("title"));
            item.setPrice(rs.getDouble("price"));
            item.setQuantity(rs.getInt("quantity"));
            item.setTotalItemPrice(rs.getDouble("total_item_price"));
            return item;
        }, username);
    }

    public void removeFromCart(String username, String isbn) {
        String sql = "DELETE FROM shopping_cart WHERE username = ? AND book_isbn = ?";
        jdbcTemplate.update(sql, username, isbn);
    }

    public void clearCart(String username) {
        String sql = "DELETE FROM shopping_cart WHERE username = ?";
        jdbcTemplate.update(sql, username);
    }
}