package com.orderprocessing.repository;

import com.orderprocessing.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByUser_UserId(Long userId);
    List<CustomerOrder> findByStatus(CustomerOrder.Status status);
    
    @Query("SELECT co FROM CustomerOrder co WHERE co.orderDate >= :startDate AND co.orderDate < :endDate")
    List<CustomerOrder> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(co.totalAmount) FROM CustomerOrder co WHERE co.orderDate >= :startDate AND co.orderDate < :endDate")
    BigDecimal sumTotalAmountBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(co) FROM CustomerOrder co WHERE co.orderDate >= :startDate AND co.orderDate < :endDate")
    Long countOrdersBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
