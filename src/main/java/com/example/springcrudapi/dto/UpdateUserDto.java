package com.example.springcrudapi.dto;

import jakarta.validation.constraints.*;

public class UpdateUserDto {
    
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @Email(message = "Email should be valid")
    private String email;
    
    @Min(value = 0, message = "Age must be at least 0")
    @Max(value = 150, message = "Age must be at most 150")
    private Integer age;
    
    private Boolean isActive;
    
    // Default constructor
    public UpdateUserDto() {}
    
    // Constructor with fields
    public UpdateUserDto(String name, String email, Integer age, Boolean isActive) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    @Override
    public String toString() {
        return "UpdateUserDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", isActive=" + isActive +
                '}';
    }
} 