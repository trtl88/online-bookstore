package com.orderprocessing.service;

import com.orderprocessing.dto.*;
import com.orderprocessing.model.User;
import com.orderprocessing.model.ShoppingCart;
import com.orderprocessing.repository.UserRepository;
import com.orderprocessing.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // In production, use password encoder
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setShippingAddress(request.getShippingAddress());
        user.setRole(User.Role.CUSTOMER);
        
        User savedUser = userRepository.save(user);
        
        // Create shopping cart for customer
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(savedUser);
        shoppingCartRepository.save(cart);
        
        return savedUser;
    }
    
    public Optional<User> login(LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
            .filter(user -> user.getPassword().equals(request.getPassword()));
    }
    
    @Transactional
    public User update(Long id, User userDetails) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setPhone(userDetails.getPhone());
        user.setShippingAddress(userDetails.getShippingAddress());
        
        return userRepository.save(user);
    }
    
    public List<User> findCustomers() {
        return userRepository.findByRole(User.Role.CUSTOMER);
    }
    
    public List<User> findAdmins() {
        return userRepository.findByRole(User.Role.ADMIN);
    }
}
