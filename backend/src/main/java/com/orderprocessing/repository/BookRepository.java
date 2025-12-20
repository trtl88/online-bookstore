package com.orderprocessing.repository;

import com.orderprocessing.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    List<Book> findByCategory(Book.Category category);
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT b FROM Book b WHERE b.quantityInStock < b.threshold")
    List<Book> findLowStockBooks();
    
    @Query("SELECT b FROM Book b WHERE b.quantityInStock = 0")
    List<Book> findOutOfStockBooks();
    
    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.name LIKE %:authorName%")
    List<Book> findByAuthorName(@Param("authorName") String authorName);
    
    @Query("SELECT b FROM Book b WHERE b.publisher.publisherId = :publisherId")
    List<Book> findByPublisher_PublisherId(@Param("publisherId") Long publisherId);
}
