package com.example.todoapi.service;

import com.example.todoapi.dto.CreateTodoDto;
import com.example.todoapi.dto.UpdateTodoDto;
import com.example.todoapi.entity.Todo;
import com.example.todoapi.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    
    private final TodoRepository todoRepository;
    
    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    
    /**
     * Get all todos
     * @return list of all todos
     */
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }
    
    /**
     * Get todo by ID
     * @param id the todo ID
     * @return the todo if found
     * @throws RuntimeException if todo not found
     */
    public Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo with ID " + id + " not found"));
    }
    
    /**
     * Create a new todo
     * @param createTodoDto the todo data
     * @return the created todo
     */
    public Todo createTodo(CreateTodoDto createTodoDto) {
        Todo todo = new Todo();
        todo.setTitle(createTodoDto.getTitle());
        todo.setDescription(createTodoDto.getDescription());
        todo.setCompleted(false);
        
        return todoRepository.save(todo);
    }
    
    /**
     * Update todo
     * @param id the todo ID
     * @param updateTodoDto the update data
     * @return the updated todo
     * @throws RuntimeException if todo not found
     */
    public Todo updateTodo(Long id, UpdateTodoDto updateTodoDto) {
        Todo todo = getTodoById(id);
        
        // Update fields if provided
        if (updateTodoDto.getTitle() != null) {
            todo.setTitle(updateTodoDto.getTitle());
        }
        if (updateTodoDto.getDescription() != null) {
            todo.setDescription(updateTodoDto.getDescription());
        }
        if (updateTodoDto.getCompleted() != null) {
            todo.setCompleted(updateTodoDto.getCompleted());
        }
        
        return todoRepository.save(todo);
    }
    
    /**
     * Toggle todo completed status
     * @param id the todo ID
     * @return the updated todo
     * @throws RuntimeException if todo not found
     */
    public Todo toggleTodo(Long id) {
        Todo todo = getTodoById(id);
        todo.setCompleted(!todo.getCompleted());
        return todoRepository.save(todo);
    }
    
    /**
     * Delete todo
     * @param id the todo ID
     * @throws RuntimeException if todo not found
     */
    public void deleteTodo(Long id) {
        Todo todo = getTodoById(id);
        todoRepository.delete(todo);
    }
}

