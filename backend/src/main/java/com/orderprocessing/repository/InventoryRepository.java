package com.orderprocessing.repository;

import com.orderprocessing.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    Optional<Inventory> findByProduct_ProductId(Long productId);
    
    @Query("SELECT i FROM Inventory i WHERE i.quantityInStock <= i.reorderLevel")
    List<Inventory> findLowStockItems();
    
    @Query("SELECT i FROM Inventory i WHERE i.quantityInStock = 0")
    List<Inventory> findOutOfStockItems();
    
    @Query("SELECT i FROM Inventory i WHERE i.quantityInStock > i.reorderLevel")
    List<Inventory> findInStockItems();
}
