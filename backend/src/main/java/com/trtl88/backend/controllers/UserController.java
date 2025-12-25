package com.trtl88.backend.controllers;

import com.trtl88.backend.models.User;
import com.trtl88.backend.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users") // Base URL: http://localhost:8080/api/users
@CrossOrigin(origins = "*")   // IMPORTANT: Allows your HTML file to connect without security blocking
public class UserController {

    private final UserService userService;

    // Constructor Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. SIGNUP
    // Usage: POST http://localhost:8080/api/users/signup
    // Body (JSON): { "username": "...", "password": "...", "firstName": "...", ... }
    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // 2. LOGIN
    // Usage: POST http://localhost:8080/api/users/login
    // Body (JSON): { "username": "myUser", "password": "myPassword" }
    @PostMapping("/login")
    public User login(@RequestBody User loginRequest) {
        // We reuse the User model to catch the username/password JSON
        return userService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    // 3. UPDATE PROFILE
    // Usage: PUT http://localhost:8080/api/users/profile
    // Body (JSON): { "username": "currentName", "password": "newPass", ... }
    @PutMapping("/profile")
    public String updateProfile(@RequestBody User user) {
        return userService.updateUser(user);
    }

    // ---------------------------------------------------------
    // MANAGER FEATURES
    // ---------------------------------------------------------

    // 4. GET ALL CUSTOMERS (So Manager can see who to promote)
    // Usage: GET http://localhost:8080/api/users/customers
    @GetMapping("/customers")
    public List<User> getAllCustomers() {
        return userService.getAllCustomers();
    }

    // 5. PROMOTE USER TO MANAGER
    // Usage: PUT http://localhost:8080/api/users/promote/john_doe
    @PutMapping("/promote/{username}")
    public String promoteUser(@PathVariable String username) {
        return userService.promoteUserToManager(username);
    }
}