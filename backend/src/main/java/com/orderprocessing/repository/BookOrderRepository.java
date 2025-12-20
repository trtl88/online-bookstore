package com.orderprocessing.repository;

import com.orderprocessing.model.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {
    List<BookOrder> findByStatus(BookOrder.Status status);
    List<BookOrder> findByBook_BookId(Long bookId);
    
    @Query("SELECT COUNT(bo) FROM BookOrder bo WHERE bo.book.bookId = :bookId")
    Long countOrdersByBookId(@Param("bookId") Long bookId);
    
    @Query("SELECT SUM(bo.quantity) FROM BookOrder bo WHERE bo.book.bookId = :bookId")
    Long sumQuantityByBookId(@Param("bookId") Long bookId);
}
