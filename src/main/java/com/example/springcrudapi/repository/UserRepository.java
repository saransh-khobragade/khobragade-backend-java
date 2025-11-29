package com.example.springcrudapi.repository;

import com.example.springcrudapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by email
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if a user exists by email
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Find all active users
     * @return list of active users
     */
    List<User> findByIsActiveTrue();
    
    /**
     * Find all inactive users
     * @return list of inactive users
     */
    List<User> findByIsActiveFalse();
} 