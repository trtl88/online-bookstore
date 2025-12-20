package com.orderprocessing.service;

import com.orderprocessing.dto.BookRequest;
import com.orderprocessing.model.Author;
import com.orderprocessing.model.Book;
import com.orderprocessing.model.Publisher;
import com.orderprocessing.repository.AuthorRepository;
import com.orderprocessing.repository.BookRepository;
import com.orderprocessing.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookService {
    
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
    
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }
    
    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
    
    public List<Book> findByCategory(Book.Category category) {
        return bookRepository.findByCategory(category);
    }
    
    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public List<Book> findLowStockBooks() {
        return bookRepository.findLowStockBooks();
    }
    
    public List<Book> findOutOfStockBooks() {
        return bookRepository.findOutOfStockBooks();
    }
    
    @Transactional
    public Book save(BookRequest request) {
        Book book = new Book();
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setPublicationYear(request.getPublicationYear());
        book.setSellingPrice(request.getSellingPrice());
        book.setCategory(request.getCategory());
        book.setQuantityInStock(request.getQuantityInStock());
        book.setThreshold(request.getThreshold());
        
        // Set publisher
        if (request.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
            book.setPublisher(publisher);
        }
        
        // Set authors
        if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
            Set<Author> authors = new HashSet<>();
            for (Long authorId : request.getAuthorIds()) {
                Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new RuntimeException("Author not found: " + authorId));
                authors.add(author);
            }
            book.setAuthors(authors);
        }
        
        return bookRepository.save(book);
    }
    
    @Transactional
    public Book update(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setPublicationYear(request.getPublicationYear());
        book.setSellingPrice(request.getSellingPrice());
        book.setCategory(request.getCategory());
        book.setQuantityInStock(request.getQuantityInStock());
        book.setThreshold(request.getThreshold());
        
        // Update publisher
        if (request.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
            book.setPublisher(publisher);
        }
        
        // Update authors
        if (request.getAuthorIds() != null) {
            Set<Author> authors = new HashSet<>();
            for (Long authorId : request.getAuthorIds()) {
                Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new RuntimeException("Author not found: " + authorId));
                authors.add(author);
            }
            book.setAuthors(authors);
        }
        
        return bookRepository.save(book);
    }
    
    @Transactional
    public void updateStock(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        
        int newStock = book.getQuantityInStock() + quantity;
        if (newStock < 0) {
            throw new RuntimeException("Cannot reduce stock below 0");
        }
        book.setQuantityInStock(newStock);
        bookRepository.save(book);
    }
    
    @Transactional
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
