package com.orderprocessing.repository;

import com.orderprocessing.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByCustomer_CustomerId(Long customerId);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.customer.customerId = :customerId AND o.status = :status")
    List<Order> findByCustomerIdAndStatus(Long customerId, Order.OrderStatus status);
    
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findAllOrdersOrderByDateDesc();
    
    @Query("SELECT o FROM Order o WHERE o.customer.customerId = :customerId ORDER BY o.orderDate DESC")
    List<Order> findByCustomerIdOrderByDateDesc(Long customerId);
}
