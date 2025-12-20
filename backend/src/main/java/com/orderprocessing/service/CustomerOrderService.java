package com.orderprocessing.service;

import com.orderprocessing.dto.CheckoutRequest;
import com.orderprocessing.model.*;
import com.orderprocessing.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {
    
    private final CustomerOrderRepository customerOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    
    public List<CustomerOrder> findAll() {
        return customerOrderRepository.findAll();
    }
    
    public Optional<CustomerOrder> findById(Long id) {
        return customerOrderRepository.findById(id);
    }
    
    public List<CustomerOrder> findByUserId(Long userId) {
        return customerOrderRepository.findByUser_UserId(userId);
    }
    
    @Transactional
    public CustomerOrder checkout(Long userId, CheckoutRequest request) {
        // Validate credit card
        if (request.getCreditCardNumber() == null || request.getCreditCardNumber().length() != 16) {
            throw new RuntimeException("Invalid credit card number. Must be 16 digits.");
        }
        
        // Get user's cart
        ShoppingCart cart = shoppingCartRepository.findByUser_UserId(userId)
            .orElseThrow(() -> new RuntimeException("Shopping cart not found"));
        
        List<CartItem> cartItems = cartItemRepository.findByCart_CartId(cart.getCartId());
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Shopping cart is empty");
        }
        
        // Validate stock availability
        for (CartItem item : cartItems) {
            Book book = item.getBook();
            if (book.getQuantityInStock() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for: " + book.getTitle());
            }
        }
        
        // Calculate total
        BigDecimal total = cartItems.stream()
            .map(item -> item.getBook().getSellingPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Get user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Create order
        CustomerOrder order = new CustomerOrder();
        order.setUser(user);
        order.setTotalAmount(total);
        order.setCreditCardLastFour(request.getCreditCardNumber().substring(12));
        order.setShippingAddress(request.getShippingAddress() != null ? 
            request.getShippingAddress() : user.getShippingAddress());
        order.setStatus(CustomerOrder.Status.PENDING);
        
        CustomerOrder savedOrder = customerOrderRepository.save(order);
        
        // Create order items and update stock
        for (CartItem cartItem : cartItems) {
            Book book = cartItem.getBook();
            
            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setBook(book);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(book.getSellingPrice());
            orderItemRepository.save(orderItem);
            
            // Update book stock
            book.setQuantityInStock(book.getQuantityInStock() - cartItem.getQuantity());
            bookRepository.save(book);
        }
        
        // Clear cart
        cartItemRepository.deleteByCart_CartId(cart.getCartId());
        
        return savedOrder;
    }
    
    @Transactional
    public CustomerOrder updateStatus(Long orderId, CustomerOrder.Status status) {
        CustomerOrder order = customerOrderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return customerOrderRepository.save(order);
    }
    
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrder_OrderId(orderId);
    }
}
