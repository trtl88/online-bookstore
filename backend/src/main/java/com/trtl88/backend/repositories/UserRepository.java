package com.trtl88.backend.repositories;

import com.trtl88.backend.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. SIGNUP: Register a new customer
    public int save(User user) {
        String sql = "INSERT INTO users (username, password, first_name, last_name, email, phone_no, shipping_address, is_admin) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
            user.getUsername(),
            user.getPassword(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getShippingAddress(),
            user.isAdmin());
    }

    // 2. LOGIN: Find user by username
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            // Note: BeanPropertyRowMapper automatically maps "is_admin" (SQL) to "isAdmin"
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // 3. EDIT PROFILE: Update customer information
    public int updateProfile(User user) {
        String sql = "UPDATE users SET password = ?, first_name = ?, last_name = ?, email = ?, phone_no = ?, shipping_address = ? "
                +
                "WHERE username = ?";
        return jdbcTemplate.update(sql,
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getShippingAddress(),
                user.getUsername());
    }

    // 4. PROMOTE USER: Manager can promote a customer to manager status
    public int promoteUser(String username) {
        String sql = "UPDATE users SET is_admin = 1 WHERE username = ?";
        return jdbcTemplate.update(sql, username);
    }

    // 5. LIST CUSTOMERS: Needed so the Manager can see who to promote
    // (Excludes existing admins so you don't promote someone twice)
    public List<User> findAllCustomers() {
        String sql = "SELECT * FROM users WHERE is_admin = 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }
}