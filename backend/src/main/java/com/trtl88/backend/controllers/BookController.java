package com.trtl88.backend.controllers;

import com.trtl88.backend.models.Book;
import com.trtl88.backend.services.BookService;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/books") // Base URL for all these commands
// @CrossOrigin(origins = "http://localhost:3000") // Allows your Frontend (React/HTML) to talk to this
// NEW (Allows your HTML file to connect)
@CrossOrigin(origins = "*")
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
    public List<Book> searchBooks(@RequestParam String query, @RequestParam(required = false) String category) {
        return bookService.searchBooks(query, category);
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
    public org.springframework.http.ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        try {
            Book b = bookService.getBookByIsbn(isbn);
            if (b == null) return org.springframework.http.ResponseEntity.notFound().build();
            return org.springframework.http.ResponseEntity.ok(b);
        } catch (RuntimeException e) {
            // Service may throw when not found — translate to 404
            return org.springframework.http.ResponseEntity.notFound().build();
        }
    }

    // 5. ADD NEW BOOK (Admin Feature)
    // Usage: POST http://localhost:8080/api/books/add
    @PostMapping("/add")
    public String addBook(@RequestBody Book book) {
        return bookService.addNewBook(book);
    }

    // 7. GET PUBLISHER NAMES (for autocomplete)
    @GetMapping("/publishers")
    public List<String> getPublishers() {
        return bookService.getAllPublisherNames();
    }

    // 8. Upload cover image
    @PostMapping("/uploadCover")
    public String uploadCover(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) return "";
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]","_");

            // Primary dev path (project source static folder)
            java.nio.file.Path srcImgDir = java.nio.file.Paths.get("src/main/resources/static/assets/img").toAbsolutePath();
            java.nio.file.Files.createDirectories(srcImgDir);
            java.nio.file.Path srcOut = srcImgDir.resolve(filename);

            // Runtime classpath static folder (when running from IDE/packaged target)
            java.nio.file.Path targetImgDir = java.nio.file.Paths.get("target/classes/static/assets/img").toAbsolutePath();
            // create target dir only if parent exists or attempt to create it as well
            try { java.nio.file.Files.createDirectories(targetImgDir); } catch(Exception ex) { /* ignore */ }
            java.nio.file.Path targetOut = targetImgDir.resolve(filename);

            // Copy stream once into a byte array, then write to both locations if available
            byte[] data;
            try (java.io.InputStream in = file.getInputStream()) {
                data = in.readAllBytes();
            }

            // write to src static folder
            try {
                java.nio.file.Files.write(srcOut, data, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);
            } catch (Exception ex) {
                // best-effort: continue to attempt target write
            }

            // write to target classes static (so running server can serve immediately)
            try {
                java.nio.file.Files.write(targetOut, data, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);
            } catch (Exception ex) {
                // ignore if cannot write to target (e.g., running from jar)
            }

            return filename;
        } catch (Exception e) {
            return "";
        }
    }

    // New: add book with optional cover in a single multipart request.
    @PostMapping(value = "/addWithCover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addBookWithCover(@RequestPart("book") String bookJson,
                                   @RequestPart(value = "file", required = false) org.springframework.web.multipart.MultipartFile file) {
        ObjectMapper mapper = new ObjectMapper();
        String filename = null;
        java.nio.file.Path srcOut = null;
        java.nio.file.Path targetOut = null;
        try {
            com.trtl88.backend.models.Book book = mapper.readValue(bookJson, com.trtl88.backend.models.Book.class);

            if (file != null && !file.isEmpty()) {
                filename = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");

                java.nio.file.Path srcImgDir = java.nio.file.Paths.get("src/main/resources/static/assets/img").toAbsolutePath();
                java.nio.file.Files.createDirectories(srcImgDir);
                srcOut = srcImgDir.resolve(filename);

                java.nio.file.Path targetImgDir = java.nio.file.Paths.get("target/classes/static/assets/img").toAbsolutePath();
                try { java.nio.file.Files.createDirectories(targetImgDir); } catch (Exception ex) { }
                targetOut = targetImgDir.resolve(filename);

                byte[] data;
                try (java.io.InputStream in = file.getInputStream()) {
                    data = in.readAllBytes();
                }
                try { java.nio.file.Files.write(srcOut, data, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.TRUNCATE_EXISTING); } catch (Exception ex) { }
                try { java.nio.file.Files.write(targetOut, data, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.TRUNCATE_EXISTING); } catch (Exception ex) { }

                book.setCoverImage("assets/img/" + filename);
            }

            String result = bookService.addNewBook(book);

            if (!result.startsWith("Success") && filename != null) {
                // delete created files on failure
                try { if (srcOut != null) java.nio.file.Files.deleteIfExists(srcOut); } catch (Exception ex) { }
                try { if (targetOut != null) java.nio.file.Files.deleteIfExists(targetOut); } catch (Exception ex) { }
            }

            return result;
        } catch (Exception e) {
            // cleanup on unexpected error
            try { if (srcOut != null) java.nio.file.Files.deleteIfExists(srcOut); } catch (Exception ex) { }
            try { if (targetOut != null) java.nio.file.Files.deleteIfExists(targetOut); } catch (Exception ex) { }
            return "Error: " + e.getMessage();
        }
    }

    // 6. UPDATE BOOK (Admin Feature)
    // Usage: PUT http://localhost:8080/api/books/update
    @PutMapping("/update")
    public String updateBook(@RequestBody Book book) {
        return bookService.updateBook(book);
    }
}