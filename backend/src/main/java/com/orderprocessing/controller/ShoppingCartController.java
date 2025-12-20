package com.orderprocessing.controller;

import com.orderprocessing.dto.CartItemRequest;
import com.orderprocessing.model.CartItem;
import com.orderprocessing.model.ShoppingCart;
import com.orderprocessing.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    
    private final ShoppingCartService shoppingCartService;
    
    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Long userId) {
        ShoppingCart cart = shoppingCartService.getOrCreateCart(userId);
        List<CartItem> items = shoppingCartService.getCartItems(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("cartId", cart.getCartId());
        response.put("userId", userId);
        response.put("items", items.stream().map(item -> {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("cartItemId", item.getCartItemId());
            itemMap.put("bookId", item.getBook().getBookId());
            itemMap.put("isbn", item.getBook().getIsbn());
            itemMap.put("title", item.getBook().getTitle());
            itemMap.put("price", item.getBook().getSellingPrice());
            itemMap.put("quantity", item.getQuantity());
            itemMap.put("subtotal", item.getSubtotal());
            return itemMap;
        }).toList());
        
        BigDecimal total = items.stream()
            .map(CartItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.put("total", total);
        response.put("itemCount", items.stream().mapToInt(CartItem::getQuantity).sum());
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{userId}/add")
    public ResponseEntity<?> addToCart(@PathVariable Long userId, @RequestBody CartItemRequest request) {
        try {
            CartItem item = shoppingCartService.addToCart(userId, request.getBookId(), request.getQuantity());
            return ResponseEntity.ok(Map.of("message", "Item added to cart", "cartItemId", item.getCartItemId()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{userId}/update")
    public ResponseEntity<?> updateCartItem(@PathVariable Long userId, @RequestBody CartItemRequest request) {
        try {
            CartItem item = shoppingCartService.updateCartItem(userId, request.getBookId(), request.getQuantity());
            if (item == null) {
                return ResponseEntity.ok(Map.of("message", "Item removed from cart"));
            }
            return ResponseEntity.ok(Map.of("message", "Cart updated", "quantity", item.getQuantity()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{userId}/remove/{bookId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long userId, @PathVariable Long bookId) {
        shoppingCartService.removeFromCart(userId, bookId);
        return ResponseEntity.ok(Map.of("message", "Item removed from cart"));
    }
    
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) {
        shoppingCartService.clearCart(userId);
        return ResponseEntity.ok(Map.of("message", "Cart cleared"));
    }
}
