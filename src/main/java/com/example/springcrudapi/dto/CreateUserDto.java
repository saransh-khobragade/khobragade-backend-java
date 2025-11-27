package com.example.springcrudapi.dto;

import jakarta.validation.constraints.*;

public class CreateUserDto {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @Min(value = 0, message = "Age must be at least 0")
    @Max(value = 150, message = "Age must be at most 150")
    private Integer age;
    
    private Boolean isActive = true;
    
    // Default constructor
    public CreateUserDto() {}
    
    // Constructor with fields
    public CreateUserDto(String name, String email, String password, Integer age, Boolean isActive) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.isActive = isActive != null ? isActive : true;
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
        return "CreateUserDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", isActive=" + isActive +
                '}';
    }
} 