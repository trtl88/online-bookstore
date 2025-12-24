package com.trtl88.backend.repository;

import com.trtl88.backend.models.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. SELECT ALL BOOKS (For the "Browse" page)
    public List<Book> findAll() {
        String sql = "SELECT b.*, p.name as publisher_name, GROUP_CONCAT(a.name) as authors " +
                     "FROM book b " +
                     "JOIN publisher p ON b.publisher_id = p.id " +
                     "LEFT JOIN book_authors ba ON b.isbn = ba.isbn " +
                     "LEFT JOIN author a ON ba.author_id = a.id " +
                     "GROUP BY b.isbn";
        return jdbcTemplate.query(sql, new BookRowMapper());
    }

    // 2. SEARCH BOOKS (Requirement: Search by ISBN, Title, Category, Author, or Publisher)
    public List<Book> searchBooks(String keyword) {
        String sql = "SELECT b.*, p.name as publisher_name, GROUP_CONCAT(a.name) as authors " +
                     "FROM book b " +
                     "JOIN publisher p ON b.publisher_id = p.id " +
                     "LEFT JOIN book_authors ba ON b.isbn = ba.isbn " +
                     "LEFT JOIN author a ON ba.author_id = a.id " +
                     "WHERE b.title LIKE ? OR b.isbn = ? OR b.category = ? OR a.name LIKE ? OR p.name LIKE ? " +
                     "GROUP BY b.isbn";
        String match = "%" + keyword + "%";
        return jdbcTemplate.query(sql, new BookRowMapper(), match, keyword, keyword, match, match);
    }

    // 3. ADD NEW BOOK (Requirement: Admin Only)
    public int save(Book book) {
        String sql = "INSERT INTO book (isbn, title, publication_year, price, category, stock_quantity, threshold, publisher_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, 
            book.getIsbn(), book.getTitle(), book.getPublicationYear(), 
            book.getPrice(), book.getCategory(), book.getStockQuantity(), 
            book.getThreshold(), book.getPublisherId());
    }

    // 4. MODIFY EXISTING BOOKS (Requirement: Admin can update any property)
    public int update(Book book) {
        String sql = "UPDATE book SET title = ?, publication_year = ?, price = ?, " +
                     "category = ?, stock_quantity = ?, threshold = ?, publisher_id = ? " +
                     "WHERE isbn = ?";
        return jdbcTemplate.update(sql, 
            book.getTitle(), book.getPublicationYear(), book.getPrice(), 
            book.getCategory(), book.getStockQuantity(), book.getThreshold(), 
            book.getPublisherId(), book.getIsbn());
    }

    // 5. LINK AUTHOR TO BOOK (Requirement: Multiple authors per book)
    public void addAuthorToBook(String isbn, int authorId) {
        String sql = "INSERT INTO book_authors (isbn, author_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, isbn, authorId);
    }

    // Internal RowMapper to bridge SQL and Java
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
            
            // These methods were added to your Book.java model
            try {
                book.setPublisherName(rs.getString("publisher_name"));
                book.setAuthorNames(rs.getString("authors"));
            } catch (SQLException e) {
                // Keep going if the query didn't include JOINs
            }
            return book;
        }
    }
}