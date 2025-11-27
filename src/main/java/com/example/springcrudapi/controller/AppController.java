package com.example.springcrudapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Application", description = "Application health and status APIs")
@CrossOrigin(origins = "*")
public class AppController {
    
    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/")
    @Operation(summary = "Root endpoint", description = "Returns a welcome message")
    @ApiResponse(responseCode = "200", description = "Welcome message")
    public String getHello() {
        return "Spring Boot CRUD API is running!";
    }
    
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Returns the application health status including database connectivity")
    @ApiResponse(responseCode = "200", description = "Health status")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "ok");
        health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        health.put("service", "Spring Boot CRUD API");
        health.put("version", "1.0.0");
        
        // Check database connectivity
        Map<String, Object> database = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            boolean isValid = connection.isValid(2); // 2 second timeout
            database.put("status", isValid ? "connected" : "disconnected");
            database.put("url", connection.getMetaData().getURL());
            health.put("database", database);
            
            if (!isValid) {
                health.put("status", "degraded");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(health);
            }
        } catch (Exception e) {
            database.put("status", "error");
            database.put("error", e.getMessage());
            health.put("database", database);
            health.put("status", "degraded");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(health);
        }
        
        return ResponseEntity.ok(health);
    }
} 