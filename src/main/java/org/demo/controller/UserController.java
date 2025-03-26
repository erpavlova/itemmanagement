package org.demo.controller;

import org.demo.model.User;
import org.demo.repository.UserRepository;
import org.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists!";
        }

        // Encode the password before saving it
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the new user
        userRepository.save(user);

        return "User registered successfully!";
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        // Only allow if the user is authenticated
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new RuntimeException("Access Denied");
        }

        return userService.getAllUsers();
    }
}
