package org.demo.controller;

import org.demo.model.User;
import org.demo.repository.UserRepository;
import org.demo.security.JwtUtil;
import org.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager; // For authenticating the user

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil; // For generating JWT token

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            // If authentication is successful, generate JWT token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully!";
    }

//    @PostMapping("/login")
//    public String login(@RequestBody Map<String, String> credentials) {
//        User user = userRepository.findByUsername(credentials.get("username"))
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (passwordEncoder.matches(credentials.get("password"), user.getPassword())) {
//            return jwtUtil.generateToken(user.getUsername());
//        } else {
//            throw new RuntimeException("Invalid credentials");
//        }
//    }
}
