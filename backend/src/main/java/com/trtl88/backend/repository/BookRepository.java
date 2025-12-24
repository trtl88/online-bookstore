package com.trtl88.backend.repository;

import com.trtl88.backend.models.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

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

    // 2. SEARCH BOOKS (Requirement: Search by ISBN, Title, Category, Author, or
    // Publisher)
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

    public List<Book> findByCategory(String category) {
        String sql = "SELECT b.*, p.name as publisher_name, GROUP_CONCAT(a.name) as authors " +
                "FROM book b " +
                "JOIN publisher p ON b.publisher_id = p.id " +
                "LEFT JOIN book_authors ba ON b.isbn = ba.isbn " +
                "LEFT JOIN author a ON ba.author_id = a.id " +
                "WHERE b.category = ? " +
                "GROUP BY b.isbn";
        return jdbcTemplate.query(sql, new BookRowMapper(), category);
    }

    // 3. ADD NEW BOOK (Requirement: Admin Only)
    public int save(Book book) {
        String sql = "INSERT INTO book (isbn, title, publication_year, price, category, stock_quantity, threshold, publisher_id) "
                +
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

    // ---------------------------------------------------------
    // FUNCTION 1: Save Authors (Complex MySQL Logic)
    // ---------------------------------------------------------
    public void saveAuthors(String isbn, List<String> authors) {
        if (authors == null || authors.isEmpty())
            return;

        for (String authorName : authors) {
            // A. Insert Author if they don't exist (MySQL 'INSERT IGNORE' skips duplicates)
            String insertAuthorSql = "INSERT IGNORE INTO author (name) VALUES (?)";
            jdbcTemplate.update(insertAuthorSql, authorName);

            // B. Fetch the Author's ID (Whether they were just added or existed before)
            String getIdSql = "SELECT author_id FROM author WHERE name = ?";
            Integer authorId = jdbcTemplate.queryForObject(getIdSql, Integer.class, authorName);

            // C. Link the Book to the Author in the middle table
            String linkSql = "INSERT INTO book_authors (book_isbn, author_id) VALUES (?, ?)";
            jdbcTemplate.update(linkSql, isbn, authorId);
        }
    }

    // ---------------------------------------------------------
    // FUNCTION 2: Get Authors (Simple Join)
    // ---------------------------------------------------------
    public List<String> findAuthorsByIsbn(String isbn) {
        String sql = "SELECT a.name FROM author a " +
                "JOIN book_authors ba ON a.author_id = ba.author_id " +
                "WHERE ba.book_isbn = ?";

        // Returns a simple list of strings like ["JK Rowling", "Stephen King"]
        return jdbcTemplate.queryForList(sql, String.class, isbn);
    }

    // Internal RowMapper to bridge SQL and Java
    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();

            // Map the columns that ACTUALLY exist in your 'book' table
            book.setIsbn(rs.getString("isbn"));
            book.setTitle(rs.getString("title"));
            book.setPublicationYear(rs.getInt("publication_year"));
            book.setPrice(rs.getDouble("price"));
            book.setCategory(rs.getString("category"));
            book.setStockQuantity(rs.getInt("stock_quantity"));
            book.setThreshold(rs.getInt("threshold"));

            // Make sure this matches the type in your Book.java (Long vs Int)
            book.setPublisherId(rs.getInt("publisher_id"));
            book.setPublisherName(rs.getString("publisher_name"));
            // DELETED: book.setAuthorNames(...)
            // Why? Because we fill this in the Service layer using
            // bookRepository.findAuthorsByIsbn()
            // DELETED: book.setPublisherName(...)
            // Why? Because this column doesn't exist in the 'book' table.

            return book;
        }
    }
}