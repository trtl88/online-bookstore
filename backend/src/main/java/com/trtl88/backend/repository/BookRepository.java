package com.trtl88.backend.repository;

import com.trtl88.backend.Book;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    // 1. Constructor Injection: Spring provides the Database Connection here
    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ----------------------------------------------------------------
    // SECTION A: READ OPERATIONS (Select)
    // ----------------------------------------------------------------

    /**
     * Get all books in the library.
     */
    public List<Book> findAll() {
        String sql = "SELECT * FROM book";
        return jdbcTemplate.query(sql, new BookRowMapper());
    }

    /**
     * Find a single book by ISBN.
     * Uses Optional in case the book is not found.
     */
    public Optional<Book> findByIsbn(String isbn) {
        String sql = "SELECT * FROM book WHERE isbn = ?";
        try {
            // queryForObject expects exactly one row, throws exception if 0 found
            Book book = jdbcTemplate.queryForObject(sql, new BookRowMapper(), isbn);
            return Optional.ofNullable(book);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Search books by Title (Partial match).
     * Example: searching "Harry" finds "Harry Potter".
     */
    public List<Book> findByTitle(String title) {
        String sql = "SELECT * FROM book WHERE title LIKE ?";
        String searchTerm = "%" + title + "%"; // Add wildcards for partial match
        return jdbcTemplate.query(sql, new BookRowMapper(), searchTerm);
    }

    /**
     * Filter books by Category (e.g., "Science").
     */
    public List<Book> findByCategory(String category) {
        String sql = "SELECT * FROM book WHERE category = ?";
        return jdbcTemplate.query(sql, new BookRowMapper(), category);
    }

    // ----------------------------------------------------------------
    // SECTION B: WRITE OPERATIONS (Insert, Update, Delete)
    // ----------------------------------------------------------------

    /**
     * Admin: Add a new book to the database.
     * Returns the number of rows affected (should be 1).
     */
    public int save(Book book) {
        String sql = "INSERT INTO book (isbn, title, publication_year, price, category, stock_quantity, threshold, publisher_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                book.getIsbn(),
                book.getTitle(),
                book.getPublicationYear(),
                book.getPrice(),
                book.getCategory(),
                book.getStockQuantity(),
                book.getThreshold(),
                book.getPublisherId()
        );
    }

    /**
     * Admin: Update an existing book's details or stock.
     * NOTE: This will trigger the MySQL triggers if stock changes!
     */
    public int update(Book book) {
        String sql = "UPDATE book SET title = ?, publication_year = ?, price = ?, category = ?, stock_quantity = ?, threshold = ?, publisher_id = ? WHERE isbn = ?";
        return jdbcTemplate.update(sql,
                book.getTitle(),
                book.getPublicationYear(),
                book.getPrice(),
                book.getCategory(),
                book.getStockQuantity(), // If this drops below threshold, MySQL Trigger fires!
                book.getThreshold(),
                book.getPublisherId(),
                book.getIsbn() // The WHERE clause parameter
        );
    }

    /**
     * Delete a book (Optional feature).
     */
    public int deleteByIsbn(String isbn) {
        String sql = "DELETE FROM book WHERE isbn = ?";
        return jdbcTemplate.update(sql, isbn);
    }

    // ----------------------------------------------------------------
    // SECTION C: ROW MAPPER (The Translator)
    // ----------------------------------------------------------------

    /**
     * This private class teaches Java how to convert a MySQL Row into a Java Book Object.
     */
    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setIsbn(rs.getString("isbn"));
            book.setTitle(rs.getString("title"));
            book.setPublicationYear(rs.getInt("publication_year"));
            book.setPrice(rs.getDouble("price"));
            book.setCategory(rs.getString("category"));
            book.setStockQuantity(rs.getInt("stock_quantity"));
            book.setThreshold(rs.getInt("threshold"));
            book.setPublisherId(rs.getLong("publisher_id"));
            return book;
        }
    }
}