package com.orderprocessing.repository;

import com.orderprocessing.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    List<OrderItem> findByOrder_OrderId(Long orderId);
    
    List<OrderItem> findByProduct_ProductId(Long productId);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId")
    List<OrderItem> findOrderItemsByOrderId(Long orderId);
}
