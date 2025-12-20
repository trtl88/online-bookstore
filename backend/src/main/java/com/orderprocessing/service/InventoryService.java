package com.orderprocessing.service;

import com.orderprocessing.model.Inventory;
import com.orderprocessing.model.Product;
import com.orderprocessing.repository.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductService productService;

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventory record not found with id: " + id));
    }

    public Inventory getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found for product id: " + productId));
    }

    public Inventory createInventory(Long productId, Integer quantity, Integer reorderLevel) {
        Product product = productService.getProductById(productId);
        
        // Check if inventory already exists
        if (inventoryRepository.findByProduct_ProductId(productId).isPresent()) {
            throw new IllegalArgumentException("Inventory already exists for product id: " + productId);
        }
        
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantityInStock(quantity);
        inventory.setReorderLevel(reorderLevel != null ? reorderLevel : 10);
        inventory.setLastRestockDate(LocalDate.now());
        
        return inventoryRepository.save(inventory);
    }

    public Inventory updateInventory(Long id, Integer quantity, Integer reorderLevel) {
        Inventory inventory = getInventoryById(id);
        
        if (quantity != null) {
            inventory.setQuantityInStock(quantity);
        }
        if (reorderLevel != null) {
            inventory.setReorderLevel(reorderLevel);
        }
        
        return inventoryRepository.save(inventory);
    }

    public Inventory restockProduct(Long productId, Integer quantity) {
        Inventory inventory = getInventoryByProductId(productId);
        inventory.setQuantityInStock(inventory.getQuantityInStock() + quantity);
        inventory.setLastRestockDate(LocalDate.now());
        return inventoryRepository.save(inventory);
    }

    public Inventory decreaseStock(Long productId, Integer quantity) {
        Inventory inventory = getInventoryByProductId(productId);
        
        if (inventory.getQuantityInStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product id: " + productId);
        }
        
        inventory.setQuantityInStock(inventory.getQuantityInStock() - quantity);
        return inventoryRepository.save(inventory);
    }

    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findLowStockItems();
    }

    public List<Inventory> getOutOfStockItems() {
        return inventoryRepository.findOutOfStockItems();
    }

    public List<Inventory> getInStockItems() {
        return inventoryRepository.findInStockItems();
    }
}
