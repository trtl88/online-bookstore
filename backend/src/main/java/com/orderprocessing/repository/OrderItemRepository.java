package com.orderprocessing.repository;

import com.orderprocessing.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder_OrderId(Long orderId);
    
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.order.orderDate >= :startDate AND oi.order.orderDate < :endDate")
    Long sumQuantityBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT oi.book.bookId, oi.book.title, SUM(oi.quantity) as totalSold " +
           "FROM OrderItem oi " +
           "WHERE oi.order.orderDate >= :startDate " +
           "GROUP BY oi.book.bookId, oi.book.title " +
           "ORDER BY totalSold DESC")
    List<Object[]> findTopSellingBooks(@Param("startDate") LocalDateTime startDate);
}
