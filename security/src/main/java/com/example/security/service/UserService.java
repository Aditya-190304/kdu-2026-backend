package com.example.security.service;

import com.example.security.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

        
        users.add(new User("basicUser", passwordEncoder.encode("password123"), "basic@example.com", List.of("ROLE_BASIC")));
        users.add(new User("adminUser", passwordEncoder.encode("admin123"), "admin@example.com", List.of("ROLE_ADMIN")));
    }

    
    public List<User> getAllUsers() {
        return users;
    }

    
    public void addUser(User user) {
    
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        users.add(user);
    }
}
