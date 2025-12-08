package com.example.taskmanagement.service;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.taskmanagement.dto.AuthResponse;
import com.example.taskmanagement.dto.LoginRequest;
import com.example.taskmanagement.dto.RegisterRequest;
import com.example.taskmanagement.entity.Role;
import com.example.taskmanagement.entity.User;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {

        try {
            if (userRepository.existsByUsername(request.getUsername())) {
                // 400 if username already exists
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
            }

            Role role = request.getRole();  // TEAM_LEAD or SD

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(role);

            // 🔴 this is where DB errors will happen, so we wrap it:
            userRepository.save(user);

            org.springframework.security.core.userdetails.UserDetails userDetails =
                    org.springframework.security.core.userdetails.User
                            .withUsername(user.getUsername())
                            .password(user.getPassword())
                            .authorities(user.getRole().name())
                            .build();

            String token = jwtService.generateToken(userDetails);

            return new AuthResponse(token, user.getUsername(), user.getRole());

        } catch (ResponseStatusException e) {
            // we already set proper status, just rethrow
            throw e;
        } catch (Exception e) {
            // any other error will come here with message
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error in register: " + e.getMessage(),
                    e
            );
        }
    }


    public AuthResponse login(LoginRequest request) {

        // authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        User user = userOpt.orElseThrow(() -> new RuntimeException("User not found"));

        org.springframework.security.core.userdetails.UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getRole().name())
                        .build();

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, user.getUsername(), user.getRole());
    }
}
