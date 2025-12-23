package com.example.todoapi.controller;

import com.example.todoapi.dto.CreateTodoDto;
import com.example.todoapi.dto.UpdateTodoDto;
import com.example.todoapi.entity.Todo;
import com.example.todoapi.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "Todos", description = "Todo management APIs")
@CrossOrigin(origins = "*")
public class TodoController {
    
    private final TodoService todoService;
    
    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    
    @GetMapping
    @Operation(summary = "Get all todos", description = "Retrieves a list of all todos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todos retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getAllTodos() {
        List<Todo> todos = todoService.getAllTodos();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", todos);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get todo by ID", description = "Retrieves a specific todo by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todo found"),
        @ApiResponse(responseCode = "404", description = "Todo not found")
    })
    public ResponseEntity<Map<String, Object>> getTodoById(
            @Parameter(description = "Todo ID", required = true) @PathVariable Long id) {
        try {
            Todo todo = todoService.getTodoById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", todo);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
    
    @PostMapping
    @Operation(summary = "Create a new todo", description = "Creates a new todo with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Todo created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request - validation failed")
    })
    public ResponseEntity<Map<String, Object>> createTodo(@Valid @RequestBody CreateTodoDto createTodoDto) {
        Todo todo = todoService.createTodo(createTodoDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", todo);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update todo", description = "Updates an existing todo's information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todo updated successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request - validation failed"),
        @ApiResponse(responseCode = "404", description = "Todo not found")
    })
    public ResponseEntity<Map<String, Object>> updateTodo(
            @Parameter(description = "Todo ID", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateTodoDto updateTodoDto) {
        try {
            Todo todo = todoService.updateTodo(id, updateTodoDto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", todo);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
    
    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Toggle todo completed status", description = "Toggles the completed status of a todo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todo status toggled successfully"),
        @ApiResponse(responseCode = "404", description = "Todo not found")
    })
    public ResponseEntity<Map<String, Object>> toggleTodo(
            @Parameter(description = "Todo ID", required = true) @PathVariable Long id) {
        try {
            Todo todo = todoService.toggleTodo(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", todo);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete todo", description = "Deletes a todo by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todo deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Todo not found")
    })
    public ResponseEntity<Map<String, Object>> deleteTodo(
            @Parameter(description = "Todo ID", required = true) @PathVariable Long id) {
        try {
            todoService.deleteTodo(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Todo deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}

