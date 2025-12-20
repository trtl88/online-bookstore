package com.orderprocessing.service;

import com.orderprocessing.model.*;
import com.orderprocessing.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    
    public Optional<ShoppingCart> getCartByUserId(Long userId) {
        return shoppingCartRepository.findByUser_UserId(userId);
    }
    
    @Transactional
    public ShoppingCart getOrCreateCart(Long userId) {
        return shoppingCartRepository.findByUser_UserId(userId)
            .orElseGet(() -> {
                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
                ShoppingCart cart = new ShoppingCart();
                cart.setUser(user);
                return shoppingCartRepository.save(cart);
            });
    }
    
    @Transactional
    public CartItem addToCart(Long userId, Long bookId, int quantity) {
        ShoppingCart cart = getOrCreateCart(userId);
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        
        // Check stock
        if (book.getQuantityInStock() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }
        
        // Check if item already in cart
        Optional<CartItem> existingItem = cartItemRepository
            .findByCart_CartIdAndBook_BookId(cart.getCartId(), bookId);
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;
            if (book.getQuantityInStock() < newQuantity) {
                throw new RuntimeException("Not enough stock available");
            }
            item.setQuantity(newQuantity);
            return cartItemRepository.save(item);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setBook(book);
            item.setQuantity(quantity);
            return cartItemRepository.save(item);
        }
    }
    
    @Transactional
    public CartItem updateCartItem(Long userId, Long bookId, int quantity) {
        ShoppingCart cart = getOrCreateCart(userId);
        
        CartItem item = cartItemRepository.findByCart_CartIdAndBook_BookId(cart.getCartId(), bookId)
            .orElseThrow(() -> new RuntimeException("Item not found in cart"));
        
        if (quantity <= 0) {
            cartItemRepository.delete(item);
            return null;
        }
        
        Book book = item.getBook();
        if (book.getQuantityInStock() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }
        
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }
    
    @Transactional
    public void removeFromCart(Long userId, Long bookId) {
        ShoppingCart cart = getOrCreateCart(userId);
        cartItemRepository.findByCart_CartIdAndBook_BookId(cart.getCartId(), bookId)
            .ifPresent(cartItemRepository::delete);
    }
    
    @Transactional
    public void clearCart(Long userId) {
        ShoppingCart cart = getOrCreateCart(userId);
        cartItemRepository.deleteByCart_CartId(cart.getCartId());
    }
    
    public List<CartItem> getCartItems(Long userId) {
        ShoppingCart cart = getOrCreateCart(userId);
        return cartItemRepository.findByCart_CartId(cart.getCartId());
    }
}
