package com.trtl88.backend.services;

import com.trtl88.backend.models.CartItem;
import com.trtl88.backend.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepo;

    public CartService(CartRepository cartRepo) {
        this.cartRepo = cartRepo;
    }

    public void addToCart(String username, String isbn, int quantity) {
        // You could add validation here (e.g., check if quantity > 0)
        cartRepo.addToCart(username, isbn, quantity);
    }

    public List<CartItem> getCartItems(String username) {
        return cartRepo.getCartItems(username);
    }

    public void removeFromCart(String username, String isbn) {
        cartRepo.removeFromCart(username, isbn);
    }

    public void clearCart(String username) {
        cartRepo.clearCart(username);
    }
}