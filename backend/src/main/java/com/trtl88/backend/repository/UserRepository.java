package com.trtl88.backend.repository;

import com.trtl88.backend.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. SIGNUP: Register a new customer (PDF Part 2, Req 1)
    public int save(User user) {
        String sql = "INSERT INTO users (username, password, first_name, last_name, email, phone_no, shipping_address, is_admin) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, 
            user.getUsername(), 
            user.getPassword(), 
            user.getFirstName(), 
            user.getLastName(), 
            user.getEmail(), 
            user.getPhoneNumber(), 
            user.getShippingAddress(), 
            user.isAdmin()
        );
    }

    // 2. LOGIN: Find user by username (Used to verify password and check isAdmin)
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            // BeanPropertyRowMapper automatically matches DB columns to Java fields
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // 3. PROFILE UPDATE: (PDF Part 2, Req 2)
    public int updateProfile(User user) {
        String sql = "UPDATE users SET password = ?, first_name = ?, last_name = ?, email = ?, phone_no = ?, shipping_address = ? " +
                     "WHERE username = ?";
        return jdbcTemplate.update(sql, 
            user.getPassword(), user.getFirstName(), user.getLastName(), 
            user.getEmail(), user.getPhoneNumber(), user.getShippingAddress(), 
            user.getUsername()
        );
    }
}
