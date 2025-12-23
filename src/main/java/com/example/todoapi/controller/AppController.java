package com.example.todoapi.controller;

import com.example.todoapi.repository.TodoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Application", description = "Application health and status APIs")
@CrossOrigin(origins = "*")
public class AppController {
    
    private final TodoRepository todoRepository;
    
    @Autowired
    public AppController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    
    @GetMapping("/")
    @Operation(summary = "Root endpoint", description = "Returns a welcome message")
    @ApiResponse(responseCode = "200", description = "Welcome message")
    public String getHello() {
        return "Todo API is running!";
    }
    
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Returns the application health status including database connectivity")
    @ApiResponse(responseCode = "200", description = "Health status")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("service", "Todo API");
        health.put("version", "1.0.0");
        health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // Test database connection
        try {
            todoRepository.count(); // Simple query to test DB connection
            health.put("status", "ok");
            health.put("database", "connected");
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            health.put("status", "error");
            health.put("database", "disconnected");
            health.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(health);
        }
    }
}

