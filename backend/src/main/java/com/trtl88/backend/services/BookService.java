package com.trtl88.backend.services;

import com.trtl88.backend.models.Book;
import com.trtl88.backend.repositories.BookRepository;
import org.springframework.transaction.annotation.Transactional;
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
        List<Book> list = bookRepository.searchBooks(isbn);
        if (list == null || list.isEmpty()) {
            throw new RuntimeException("Book not found with ISBN: " + isbn);
        }
        Book book = list.get(0);
        // populate authors for detail view
        List<String> authors = bookRepository.findAuthorsByIsbn(book.getIsbn());
        book.setAuthorNames(authors);
        return book;

    }

    /**
     * ADMIN ONLY: Add a new book to the store.
     * Includes validation logic.
     */
    @Transactional // <--- Add this annotation
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
        // Ensure publisher exists: frontend may send publisher name inside Book.publisher
        try {
            if (book.getPublisherId() == 0 && book.getPublisher() != null && book.getPublisher().getName() != null) {
                int pid = bookRepository.findOrCreatePublisher(book.getPublisher().getName());
                book.setPublisherId(pid);
            }
        } catch (Exception e) {
            // If anything goes wrong resolving publisher, return an error to the caller
            return "Error: Unable to resolve or create publisher: " + e.getMessage();
        }

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
    @Transactional // <--- Add this here too just in case
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
        return searchBooks(query, null);
    }

    // Overloaded search that accepts optional category filter
    public List<Book> searchBooks(String query, String category) {
        // Trim inputs
        String q = (query == null) ? "" : query.trim();
        String cat = (category == null) ? "" : category.trim();

        // If both empty, return all books
        if (q.isEmpty() && cat.isEmpty()) {
            return bookRepository.findAll();
        }

        List<Book> books;
        if (!q.isEmpty() && !cat.isEmpty()) {
            // Search within category
            books = bookRepository.searchBooks(q, cat);
        } else if (!q.isEmpty()) {
            books = bookRepository.searchBooks(q);
        } else {
            books = bookRepository.findByCategory(cat);
        }

        if (books == null || books.isEmpty()) return books;
        for (Book b : books) {
            List<String> authors = bookRepository.findAuthorsByIsbn(b.getIsbn());
            b.setAuthorNames(authors);
        }
        return books;
    }

    /**
     * Filter by Category.
     * [cite_start]* [cite: 46] "User can search for books of a specific Category"
     */
    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategory(category.trim());
    }
}