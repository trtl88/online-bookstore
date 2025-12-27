package com.trtl88.backend.services;

import com.trtl88.backend.models.User;
import com.trtl88.backend.repositories.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor Injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 1. SIGNUP LOGIC
     * Checks if username exists before saving.
     */
    public String registerUser(User user) {
        // A. Validate input
        if (user.getUsername() == null || user.getPassword() == null) {
            return "Error: Username and Password are required.";
        }

        

        // B. Check if username is already taken
        // (We use the findByUsername method you wrote in the Repository)
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Error: Username already exists.";
        }

        // C. Save the user
        int result = userRepository.save(user);

        if (result > 0) {
            return "Success: User registered.";
        } else {
            return "Error: Database failed to save user.";
        }
    }

    /**
     * 2. LOGIN LOGIC
     * returns the User object if successful, or null if failed.
     */
    public User login(String username, String password) {
        // A. Find the user in the DB
        Optional<User> userOpt = userRepository.findByUsername(username);

        // B. Check if user exists AND if password matches
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Note: In a real app, you would use BCrypt to check hashed passwords.
            // For this project, we are comparing plain text as per your requirements.
            if (user.getPassword().equals(password)) {
                return user; // Login Success!
            }
        }

        return null; // Login Failed
    }

    /**
     * 3. UPDATE PROFILE
     */
    public String updateUser(User user) {
        // We assume the username cannot be changed, so we use it to find the record
        int result = userRepository.updateProfile(user);

        if (result > 0) {
            return "Success: Profile updated.";
        }
        return "Error: Update failed.";
    }

    /**
     * 4. PROMOTE USER (Manager Feature)
     */
    public String promoteUserToManager(String username) {
        int result = userRepository.promoteUser(username);
        if (result > 0) {
            return "Success: User " + username + " is now a Manager.";
        }
        return "Error: Could not promote user.";
    }

    /**
     * 5. GET ALL CUSTOMERS (So Manager can see who to promote)
     */
    public List<User> getAllCustomers() {
        return userRepository.findAllCustomers();
    }
}
