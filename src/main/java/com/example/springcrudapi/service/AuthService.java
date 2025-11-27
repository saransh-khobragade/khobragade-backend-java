package com.example.springcrudapi.service;

import com.example.springcrudapi.dto.CreateUserDto;
import com.example.springcrudapi.dto.LoginDto;
import com.example.springcrudapi.dto.SignupDto;
import com.example.springcrudapi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public AuthService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Authenticate user login
     * @param loginDto login credentials
     * @return user data if authentication successful
     * @throws RuntimeException if authentication fails
     */
    public Map<String, Object> login(LoginDto loginDto) {
        // Find user by email
        User user = userService.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        
        // Check password
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        // Create response
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("age", user.getAge());
        userData.put("created_at", user.getCreatedAt());
        userData.put("updated_at", user.getUpdatedAt());
        
        response.put("data", userData);
        
        return response;
    }
    
    /**
     * Register a new user
     * @param signupDto registration data
     * @return user data if registration successful
     * @throws RuntimeException if registration fails
     */
    public Map<String, Object> signup(SignupDto signupDto) {
        // Check if user already exists
        if (userService.existsByEmail(signupDto.getEmail())) {
            throw new RuntimeException("User with email " + signupDto.getEmail() + " already exists");
        }
        
        // Create user DTO
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName(signupDto.getName());
        createUserDto.setEmail(signupDto.getEmail());
        createUserDto.setPassword(signupDto.getPassword());
        createUserDto.setAge(signupDto.getAge());
        createUserDto.setIsActive(true);
        
        // Create user
        User user = userService.createUser(createUserDto);
        
        // Create response
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("age", user.getAge());
        userData.put("created_at", user.getCreatedAt());
        userData.put("updated_at", user.getUpdatedAt());
        
        response.put("data", userData);
        
        return response;
    }
} 