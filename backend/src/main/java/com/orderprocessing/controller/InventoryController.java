package com.orderprocessing.controller;

import com.orderprocessing.dto.InventoryRequest;
import com.orderprocessing.model.Inventory;
import com.orderprocessing.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Inventory> getInventoryByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Inventory>> getLowStockItems() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<Inventory>> getOutOfStockItems() {
        return ResponseEntity.ok(inventoryService.getOutOfStockItems());
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<Inventory>> getInStockItems() {
        return ResponseEntity.ok(inventoryService.getInStockItems());
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody InventoryRequest request) {
        return new ResponseEntity<>(
                inventoryService.createInventory(
                        request.getProductId(), 
                        request.getQuantity(), 
                        request.getReorderLevel()
                ), 
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, 
                                                      @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(
                inventoryService.updateInventory(id, request.getQuantity(), request.getReorderLevel())
        );
    }

    @PostMapping("/restock/{productId}")
    public ResponseEntity<Inventory> restockProduct(@PathVariable Long productId, 
                                                     @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.restockProduct(productId, quantity));
    }
}
