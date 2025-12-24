package com.trtl88.backend.services;

import com.trtl88.backend.models.Book;
import com.trtl88.backend.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    // Constructor Injection (Connects Service to Repository)
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Get all books for the main page.
     */
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        // 2. Loop through them and fill the empty author lists
        for (Book b : books) {
            List<String> authors = bookRepository.findAuthorsByIsbn(b.getIsbn()); // <--- ADD THIS LINE
            b.setAuthorNames(authors);
        }

        return books;
    }

    /**
     * Get a single book's details (e.g., when clicking on a book).
     */
    public Book getBookByIsbn(String isbn) {
        Book book = bookRepository.searchBooks(isbn).get(0);
        if (book != null) {
            return book;
        } else {
            throw new RuntimeException("Book not found with ISBN: " + isbn);
        }

    }

    /**
     * ADMIN ONLY: Add a new book to the store.
     * Includes validation logic.
     */
    public String addNewBook(Book book) {
        // 1. Validate Price
        if (book.getPrice() < 0) {
            return "Error: Price cannot be negative.";
        }

        // 2. Validate Threshold
        if (book.getThreshold() < 0) {
            return "Error: Threshold cannot be negative.";
        }

        // 3. Check if book already exists
        List<Book> existing = bookRepository.searchBooks(book.getIsbn());
        if (!existing.isEmpty()) {
            return "Error: A book with this ISBN already exists.";
        }

        // 4. Save to Database
        int result = bookRepository.save(book);

        if (result > 0) {
            bookRepository.saveAuthors(book.getIsbn(), book.getAuthors());
            return "Success: Book added successfully.";
        } else {
            return "Error: Database failed to save the book.";
        }
    }

    /**
     * ADMIN ONLY: Update an existing book.
     * Note: If stock drops below threshold, the MySQL Trigger will handle the
     * auto-order.
     */
    public String updateBook(Book book) {
        // Logic to prevent updating a non-existent book
        if (bookRepository.searchBooks(book.getIsbn()).isEmpty()) {
            return "Error: Book not found.";
        }

        int result = bookRepository.update(book);
        return (result > 0) ? "Success: Book updated." : "Error: Update failed.";
    }

    /**
     * Search Feature: Handles searching by Title or ISBN.
     * [cite_start]* [cite: 45] "User can search for a book by ISBN and title"
     */
    public List<Book> searchBooks(String query) {
        // Null/empty-safe search. The repository-level `searchBooks` already
        // supports searching by ISBN, title, category, author or publisher,
        // so delegate to it after trimming the input. If the query is empty
        // return all books to avoid surprising empty results.
        if (query == null || query.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.searchBooks(query.trim());
    }

    /**
     * Filter by Category.
     * [cite_start]* [cite: 46] "User can search for books of a specific Category"
     */
    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategory(category.trim());
    }
}