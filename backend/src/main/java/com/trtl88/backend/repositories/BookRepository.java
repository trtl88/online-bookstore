package com.trtl88.backend.repositories;

import com.trtl88.backend.models.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;

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
        String isbnDigits = (keyword == null) ? "" : keyword.replaceAll("[^0-9]", "");
        String sql = "SELECT b.*, p.name as publisher_name, GROUP_CONCAT(a.name) as authors " +
                "FROM book b " +
                "JOIN publisher p ON b.publisher_id = p.id " +
                "LEFT JOIN book_authors ba ON b.isbn = ba.isbn " +
                "LEFT JOIN author a ON ba.author_id = a.id " +
            "WHERE b.title LIKE ? OR b.isbn = ? OR b.isbn = ? OR b.category = ? OR a.name LIKE ? OR p.name LIKE ? " +
                "GROUP BY b.isbn";
        String match = "%" + keyword + "%";
        return jdbcTemplate.query(sql, new BookRowMapper(), match, keyword, isbnDigits, keyword, match, match);
    }

    // Overloaded: search with category constraint
    public List<Book> searchBooks(String keyword, String category) {
        String isbnDigits = (keyword == null) ? "" : keyword.replaceAll("[^0-9]", "");
        String sql = "SELECT b.*, p.name as publisher_name, GROUP_CONCAT(a.name) as authors " +
                "FROM book b " +
                "JOIN publisher p ON b.publisher_id = p.id " +
                "LEFT JOIN book_authors ba ON b.isbn = ba.isbn " +
                "LEFT JOIN author a ON ba.author_id = a.id " +
                "WHERE b.category = ? AND (b.title LIKE ? OR b.isbn = ? OR b.isbn = ? OR a.name LIKE ? OR p.name LIKE ?) " +
                "GROUP BY b.isbn";
        String match = "%" + keyword + "%";
        return jdbcTemplate.query(sql, new BookRowMapper(), category, match, keyword, isbnDigits, match, match);
    }

    // Exact ISBN lookup (digits-only stored)
    public Book findByIsbn(String isbn) {
        String sql = "SELECT b.*, p.name as publisher_name, GROUP_CONCAT(a.name) as authors " +
                "FROM book b " +
                "JOIN publisher p ON b.publisher_id = p.id " +
                "LEFT JOIN book_authors ba ON b.isbn = ba.isbn " +
                "LEFT JOIN author a ON ba.author_id = a.id " +
                "WHERE b.isbn = ? " +
                "GROUP BY b.isbn";
        List<Book> list = jdbcTemplate.query(sql, new BookRowMapper(), isbn);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    public boolean existsByIsbn(String isbn) {
        String sql = "SELECT COUNT(*) FROM book WHERE isbn = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, isbn);
        return count != null && count > 0;
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
        String sql = "INSERT INTO book (isbn, title, publication_year, cover_image, price, category, stock_quantity, threshold, publisher_id) "
            +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
            book.getIsbn(), book.getTitle(), book.getPublicationYear(), book.getCoverImage(),
            book.getPrice(), book.getCategory(), book.getStockQuantity(),
            book.getThreshold(), book.getPublisherId());
    }

    /**
     * Find a publisher by name or create it and return its id.
     */
    public int findOrCreatePublisher(String name) {
        return findOrCreatePublisher(name, null, null);
    }

    public int findOrCreatePublisher(String name, String address, String phone) {
        if (name == null || name.trim().isEmpty()) return 0;
        String findSql = "SELECT id FROM publisher WHERE name = ?";
        List<Integer> ids = jdbcTemplate.queryForList(findSql, Integer.class, name);
        if (!ids.isEmpty()) return ids.get(0);

        // Insert new publisher and return generated id
        String insertSql = "INSERT INTO publisher (name, address, phone_number) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, phone);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return (key != null) ? key.intValue() : 0;
    }

    // Check if publisher exists by exact name
    public boolean existsPublisherByName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        String sql = "SELECT COUNT(*) FROM publisher WHERE name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    // 4. MODIFY EXISTING BOOKS (Requirement: Admin can update any property)
    public int update(Book book) {
        String sql = "UPDATE book SET title = ?, publication_year = ?, cover_image = ?, price = ?, " +
            "category = ?, stock_quantity = ?, threshold = ?, publisher_id = ? " +
            "WHERE isbn = ?";
        return jdbcTemplate.update(sql,
            book.getTitle(), book.getPublicationYear(), book.getCoverImage(), book.getPrice(),
            book.getCategory(), book.getStockQuantity(), book.getThreshold(),
            book.getPublisherId(), book.getIsbn());
    }

    // SAVE AUTHORS FOR A BOOK
    public void saveAuthors(String isbn, List<String> authors) {
        if (authors == null || authors.isEmpty())
            return;

        for (String authorName : authors) {
            if (authorName == null) continue;
            authorName = authorName.trim();
            if (authorName.isEmpty()) continue;
            // A. Insert Author if they don't exist (MySQL 'INSERT IGNORE' skips duplicates)
            String insertAuthorSql = "INSERT IGNORE INTO author (name) VALUES (?)";
            jdbcTemplate.update(insertAuthorSql, authorName);

            // B. Fetch the Author's ID (Whether they were just added or existed before)
            String getIdSql = "SELECT id FROM author WHERE name = ?";
            Integer authorId = jdbcTemplate.queryForObject(getIdSql, Integer.class, authorName);

            // C. Link the Book to the Author in the middle table
            String linkSql = "INSERT IGNORE INTO book_authors (isbn, author_id) VALUES (?, ?)";
            jdbcTemplate.update(linkSql, isbn, authorId);
        }
    }

    // Get authors for a specific book by ISBN
    public List<String> findAuthorsByIsbn(String isbn) {
        String sql = "SELECT a.name FROM author a " +
            "JOIN book_authors ba ON a.id = ba.author_id " +
            "WHERE ba.isbn = ?";

        // Returns a simple list of strings like ["JK Rowling", "Stephen King"]
        return jdbcTemplate.queryForList(sql, String.class, isbn);
    }

    // Get all publisher names for autocomplete
    public List<String> findAllPublisherNames() {
        String sql = "SELECT name FROM publisher";
        return jdbcTemplate.queryForList(sql, String.class);
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
            try { book.setCoverImage(rs.getString("cover_image")); } catch(Exception e) {}
            book.setPrice(rs.getDouble("price"));
            book.setCategory(rs.getString("category"));
            book.setStockQuantity(rs.getInt("stock_quantity"));
            book.setThreshold(rs.getInt("threshold"));

            // Make sure this matches the type in your Book.java (Long vs Int)
            book.setPublisherId(rs.getInt("publisher_id"));
            book.setPublisherName(rs.getString("publisher_name"));

            return book;
        }
    }
}