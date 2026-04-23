package com.todorails.service;

import com.todorails.model.User;
import com.todorails.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service                    // tells Spring: this is a service class
@RequiredArgsConstructor    // Lombok: auto-generates constructor for final fields
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Check if username is already taken
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // Check if email is already taken
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Save a new user
    public void registerUser(User user) {
        // NEVER store plain text password
        // BCrypt turns "mypassword" into "$2a$10$xyz..." (one-way hash)
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // get the full User object from the database by username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}