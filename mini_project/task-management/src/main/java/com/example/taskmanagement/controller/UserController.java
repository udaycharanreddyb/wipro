package com.example.taskmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.taskmanagement.dto.AuthResponse;
import com.example.taskmanagement.dto.LoginRequest;
import com.example.taskmanagement.dto.RegisterRequest;
import com.example.taskmanagement.service.AuthService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    // 1. Register
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // 2. Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // Example protected endpoint to test token
    @GetMapping("/me")
    public ResponseEntity<String> me() {
        return ResponseEntity.ok("You are authenticated!");
    }
}
