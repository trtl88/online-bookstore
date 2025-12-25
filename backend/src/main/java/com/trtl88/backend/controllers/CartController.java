package com.trtl88.backend.controllers;

import com.trtl88.backend.models.CartItem;
import com.trtl88.backend.services.CartService; // Use Service
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService; // Connected to Service

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam String username,
            @RequestParam String isbn,
            @RequestParam int quantity) {
        cartService.addToCart(username, isbn, quantity);
        return "Success: Item added to cart.";
    }

    @GetMapping("/{username}")
    public List<CartItem> viewCart(@PathVariable String username) {
        return cartService.getCartItems(username);
    }

    @DeleteMapping("/remove")
    public String removeFromCart(@RequestParam String username, @RequestParam String isbn) {
        cartService.removeFromCart(username, isbn);
        return "Success: Item removed.";
    }

    @DeleteMapping("/clear")
    public String clearCart(@RequestParam String username) {
        cartService.clearCart(username);
        return "Success: Cart cleared.";
    }
}