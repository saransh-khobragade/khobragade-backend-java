package com.example.springcrudapi.controller;

import com.example.springcrudapi.dto.LoginDto;
import com.example.springcrudapi.dto.SignupDto;
import com.example.springcrudapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates a user with email and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User logged in successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request - validation failed"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            Map<String, Object> response = authService.login(loginDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
    
    @PostMapping("/signup")
    @Operation(summary = "User registration", description = "Registers a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request - validation failed"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody SignupDto signupDto) {
        try {
            Map<String, Object> response = authService.signup(signupDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }
} 