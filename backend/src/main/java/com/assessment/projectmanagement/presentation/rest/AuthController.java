package com.assessment.projectmanagement.presentation.rest;

import com.assessment.projectmanagement.domain.exception.BusinessRuleException;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.infrastructure.adapter.security.JwtTokenProvider;
import com.assessment.projectmanagement.infrastructure.adapter.persistence.UserJpaRepository;
import com.assessment.projectmanagement.infrastructure.entity.UserEntity;
import com.assessment.projectmanagement.presentation.dto.AuthResponse;
import com.assessment.projectmanagement.presentation.dto.LoginRequest;
import com.assessment.projectmanagement.presentation.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

/**
 * REST Controller for authentication endpoints
 * Handles user registration and login
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthController {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(
            UserJpaRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessRuleException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessRuleException("Email already exists");
        }

        // Create new user
        UserEntity userEntity = new UserEntity(
                UUID.randomUUID(),
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()));

        userRepository.save(userEntity);

        // Create UserDetails for JWT generation
        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                new ArrayList<>());

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(userDetails, userEntity.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, userEntity.getUsername(), userEntity.getEmail()));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        // Get user details
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessRuleException("User not found"));

        // Create UserDetails for JWT generation
        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>());

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(userDetails, user.getId());

        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getEmail()));
    }
}
