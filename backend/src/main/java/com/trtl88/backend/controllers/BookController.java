package com.trtl88.backend.controllers;

import com.trtl88.backend.models.Book;
import com.trtl88.backend.services.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books") // Base URL for all these commands
@CrossOrigin(origins = "http://localhost:3000") // Allows your Frontend (React/HTML) to talk to this
public class BookController {

    private final BookService bookService;

    // Constructor Injection
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // 1. GET ALL BOOKS (Home Page)
    // Usage: GET http://localhost:8080/api/books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // 2. SEARCH BOOKS (Search Bar)
    // Usage: GET http://localhost:8080/api/books/search?query=Harry
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String query) {
        return bookService.searchBooks(query);
    }

    // 3. GET BOOKS BY CATEGORY (Filter)
    // Usage: GET http://localhost:8080/api/books/category/Science
    @GetMapping("/category/{category}")
    public List<Book> getBooksByCategory(@PathVariable String category) {
        return bookService.getBooksByCategory(category);
    }

    // 4. GET SINGLE BOOK DETAILS
    // Usage: GET http://localhost:8080/api/books/123-456-789
    @GetMapping("/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    // 5. ADD NEW BOOK (Admin Feature)
    // Usage: POST http://localhost:8080/api/books/add
    @PostMapping("/add")
    public String addBook(@RequestBody Book book) {
        return bookService.addNewBook(book);
    }

    // 6. UPDATE BOOK (Admin Feature)
    // Usage: PUT http://localhost:8080/api/books/update
    @PutMapping("/update")
    public String updateBook(@RequestBody Book book) {
        return bookService.updateBook(book);
    }
}