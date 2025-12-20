package com.orderprocessing.controller;

import com.orderprocessing.dto.LoginRequest;
import com.orderprocessing.dto.RegisterRequest;
import com.orderprocessing.model.User;
import com.orderprocessing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getUserId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            response.put("message", "Registration successful");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.login(request)
            .map(user -> {
                Map<String, Object> response = new HashMap<>();
                response.put("userId", user.getUserId());
                response.put("username", user.getUsername());
                response.put("email", user.getEmail());
                response.put("firstName", user.getFirstName());
                response.put("lastName", user.getLastName());
                response.put("role", user.getRole());
                response.put("shippingAddress", user.getShippingAddress());
                response.put("message", "Login successful");
                return ResponseEntity.ok(response);
            })
            .orElse(ResponseEntity.status(401).body(
                Map.of("error", "Invalid username or password")
            ));
    }
    
    @PostMapping("/logout/{userId}")
    public ResponseEntity<?> logout(@PathVariable Long userId) {
        // In a real app, this would invalidate the session/token
        // For now, just return success
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
}
