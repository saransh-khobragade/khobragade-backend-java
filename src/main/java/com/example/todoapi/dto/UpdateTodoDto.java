package com.example.todoapi.dto;

import jakarta.validation.constraints.Size;

public class UpdateTodoDto {
    
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;
    
    private String description;
    
    private Boolean completed;
    
    // Default constructor
    public UpdateTodoDto() {}
    
    // Constructor with fields
    public UpdateTodoDto(String title, String description, Boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getCompleted() {
        return completed;
    }
    
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    
    @Override
    public String toString() {
        return "UpdateTodoDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                '}';
    }
}

