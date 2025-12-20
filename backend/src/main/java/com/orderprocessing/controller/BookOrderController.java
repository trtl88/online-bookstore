package com.orderprocessing.controller;

import com.orderprocessing.model.BookOrder;
import com.orderprocessing.service.BookOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book-orders")
@RequiredArgsConstructor
public class BookOrderController {
    
    private final BookOrderService bookOrderService;
    
    @GetMapping
    public ResponseEntity<List<BookOrder>> getAllOrders() {
        return ResponseEntity.ok(bookOrderService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookOrder> getOrderById(@PathVariable Long id) {
        return bookOrderService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/pending")
    public ResponseEntity<List<BookOrder>> getPendingOrders() {
        return ResponseEntity.ok(bookOrderService.findPendingOrders());
    }
    
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BookOrder>> getOrdersByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookOrderService.findByBookId(bookId));
    }
    
    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> body) {
        try {
            Long bookId = Long.valueOf(body.get("bookId").toString());
            int quantity = body.get("quantity") != null ? 
                Integer.parseInt(body.get("quantity").toString()) : 10;
            
            BookOrder order = bookOrderService.placeOrder(bookId, quantity);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirmOrder(@PathVariable Long id) {
        try {
            BookOrder order = bookOrderService.confirmOrder(id);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/book/{bookId}/count")
    public ResponseEntity<Map<String, Object>> getOrderCount(@PathVariable Long bookId) {
        Long count = bookOrderService.getOrderCountByBook(bookId);
        Long totalQuantity = bookOrderService.getTotalQuantityOrderedByBook(bookId);
        return ResponseEntity.ok(Map.of(
            "bookId", bookId,
            "timesOrdered", count,
            "totalQuantityOrdered", totalQuantity
        ));
    }
}
