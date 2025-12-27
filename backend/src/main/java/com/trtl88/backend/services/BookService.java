package com.trtl88.backend.services;

import com.trtl88.backend.models.Book;
import com.trtl88.backend.repositories.BookRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Get all books for the main page.
     */
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        for (Book b : books) {
            List<String> authors = bookRepository.findAuthorsByIsbn(b.getIsbn());
            b.setAuthorNames(authors);
        }
        return books;
    }

    /**
     * Get a single book's details (e.g., when clicking on a book).
     * Returns null if not found.
     */
    public Book getBookByIsbn(String isbn) {
        String normalizedIsbn = normalizeIsbnDigitsOnly(isbn);
        if (normalizedIsbn == null || normalizedIsbn.isBlank()) {
            return null;
        }

        Book book = bookRepository.findByIsbn(normalizedIsbn);
        if (book == null) {
            return null;
        }
        List<String> authors = bookRepository.findAuthorsByIsbn(book.getIsbn());
        book.setAuthorNames(authors);
        return book;
    }

    /**
     * ADMIN ONLY: Add a new book to the store.
     * Includes validation logic.
     */
    @Transactional
    public String addNewBook(Book book) {
        if (book == null) return "Error: Book payload is missing.";

        // Normalize ISBN: remove any non-digit characters
        String rawIsbn = normalizeIsbnDigitsOnly(book.getIsbn());
        if (!rawIsbn.matches("^\\d{13}$")) {
            return "Error: ISBN must be 13 digits.";
        }
        book.setIsbn(rawIsbn);

        // Basic numeric validations
        if (book.getPrice() < 0) return "Error: Price cannot be negative.";
        if (book.getThreshold() < 0) return "Error: Threshold cannot be negative.";
        if (book.getStockQuantity() < 0) return "Error: Stock quantity cannot be negative.";

        // Basic content validations
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) return "Error: Title is required.";
        if (book.getAuthors() == null || book.getAuthors().isEmpty()) return "Error: Please provide at least one author.";
        if (book.getCategory() == null || book.getCategory().trim().isEmpty()) return "Error: Category is required.";

        // Uniqueness check
        if (bookRepository.existsByIsbn(book.getIsbn())) return "Error: A book with this ISBN already exists.";

        // Publication year
        int currentYear = Year.now().getValue();
        if (book.getPublicationYear() > currentYear) return "Error: Publication year cannot be in the future.";

        // Ensure publisher exists or create it when necessary
        try {
            if (book.getPublisherId() == 0 && book.getPublisher() != null) {
                String pname = book.getPublisher().getName();
                String paddr = book.getPublisher().getAddress();
                String pphone = book.getPublisher().getPhoneNumber();

                boolean exists = bookRepository.existsPublisherByName(pname);
                if (!exists) {
                    if (pname == null || pname.trim().isEmpty()) {
                        return "Error: Publisher name is required.";
                    }
                    if (paddr == null || paddr.trim().isEmpty() || pphone == null || pphone.trim().isEmpty()) {
                        return "Error: Publisher not found: please provide publisher address and phone to create a new publisher";
                    }
                }
                int pid = bookRepository.findOrCreatePublisher(pname, paddr, pphone);
                book.setPublisherId(pid);
            }
        } catch (Exception e) {
            return "Error: Unable to resolve or create publisher: " + e.getMessage();
        }

        int result = bookRepository.save(book);
        if (result > 0) {
            bookRepository.saveAuthors(book.getIsbn(), book.getAuthors());
            return "Success: Book added successfully.";
        }
        return "Error: Database failed to save the book.";
    }

    @Transactional
    public String updateBook(Book book) {
        if (book == null) {
            return "Error: Book payload is missing.";
        }

        String normalizedIsbn = normalizeIsbnDigitsOnly(book.getIsbn());
        if (normalizedIsbn == null || normalizedIsbn.isBlank()) {
            return "Error: ISBN is required.";
        }
        book.setIsbn(normalizedIsbn);

        if (!bookRepository.existsByIsbn(book.getIsbn())) {
            return "Error: Book not found.";
        }
        int result = bookRepository.update(book);
        return (result > 0) ? "Success: Book updated." : "Error: Update failed.";
    }

    private String normalizeIsbnDigitsOnly(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("[^0-9]", "");
    }

    public List<Book> searchBooks(String query) {
        return searchBooks(query, null);
    }

    public List<Book> searchBooks(String query, String category) {
        String q = (query == null) ? "" : query.trim();
        String cat = (category == null) ? "" : category.trim();
        if (q.isEmpty() && cat.isEmpty()) return bookRepository.findAll();

        List<Book> books;
        if (!q.isEmpty() && !cat.isEmpty()) {
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

    public List<String> getAllPublisherNames() {
        return bookRepository.findAllPublisherNames();
    }

    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategory(category == null ? "" : category.trim());
    }
}