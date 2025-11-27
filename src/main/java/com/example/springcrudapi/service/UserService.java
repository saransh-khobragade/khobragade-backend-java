package com.example.springcrudapi.service;

import com.example.springcrudapi.dto.CreateUserDto;
import com.example.springcrudapi.dto.UpdateUserDto;
import com.example.springcrudapi.entity.User;
import com.example.springcrudapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Create a new user
     * @param createUserDto the user data
     * @return the created user
     */
    public User createUser(CreateUserDto createUserDto) {
        // Check if user already exists
        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new RuntimeException("User with email " + createUserDto.getEmail() + " already exists");
        }
        
        // Create new user
        User user = new User();
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user.setAge(createUserDto.getAge());
        user.setIsActive(createUserDto.getIsActive() != null ? createUserDto.getIsActive() : true);
        
        return userRepository.save(user);
    }
    
    /**
     * Get all users
     * @return list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Get user by ID
     * @param id the user ID
     * @return the user if found
     * @throws RuntimeException if user not found
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found"));
    }
    
    /**
     * Update user
     * @param id the user ID
     * @param updateUserDto the update data
     * @return the updated user
     * @throws RuntimeException if user not found
     */
    public User updateUser(Long id, UpdateUserDto updateUserDto) {
        User user = getUserById(id);
        
        // Update fields if provided
        if (updateUserDto.getName() != null) {
            user.setName(updateUserDto.getName());
        }
        if (updateUserDto.getEmail() != null) {
            // Check if email is already taken by another user
            Optional<User> existingUser = userRepository.findByEmail(updateUserDto.getEmail());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                throw new RuntimeException("Email " + updateUserDto.getEmail() + " is already taken");
            }
            user.setEmail(updateUserDto.getEmail());
        }
        if (updateUserDto.getAge() != null) {
            user.setAge(updateUserDto.getAge());
        }
        if (updateUserDto.getIsActive() != null) {
            user.setIsActive(updateUserDto.getIsActive());
        }
        
        return userRepository.save(user);
    }
    
    /**
     * Delete user
     * @param id the user ID
     * @throws RuntimeException if user not found
     */
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
    
    /**
     * Find user by email
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Check if user exists by email
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
} 