package com.example.springcrudapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class DataSourceConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    /**
     * Configure DataSource to handle Render's DATABASE_URL format
     * 
     * Render provides: postgresql://user:password@host:port/database?sslmode=require
     * Spring Boot expects: jdbc:postgresql://host:port/database?sslmode=require
     * 
     * If DATABASE_URL is provided (Render), parse and convert it.
     * Otherwise, use individual environment variables (local development with docker-compose)
     */
    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        // If DATABASE_URL is provided (Render) and not already in JDBC format, convert it
        if (StringUtils.hasText(databaseUrl) && !databaseUrl.startsWith("jdbc:")) {
            try {
                // Handle URL encoding in password
                String decodedUrl = URLDecoder.decode(databaseUrl, StandardCharsets.UTF_8);
                
                // Parse Render's DATABASE_URL format: postgresql://user:password@host:port/database?params
                URI dbUri = new URI(decodedUrl);
                
                // Extract username and password from userInfo
                String username = "";
                String password = "";
                if (dbUri.getUserInfo() != null) {
                    String[] userInfo = dbUri.getUserInfo().split(":");
                    username = userInfo[0];
                    if (userInfo.length > 1) {
                        password = userInfo[1];
                    }
                }
                
                String host = dbUri.getHost();
                int port = dbUri.getPort() == -1 ? 5432 : dbUri.getPort();
                String database = dbUri.getPath() != null && dbUri.getPath().length() > 1 ? 
                    dbUri.getPath().substring(1) : ""; // Remove leading '/'
                
                // Ensure SSL mode is set for Render (required for Render PostgreSQL)
                String query = dbUri.getQuery() != null ? dbUri.getQuery() : "sslmode=require";
                if (!query.contains("sslmode")) {
                    query += (query.contains("&") ? "&" : "") + "sslmode=require";
                }
                
                // Add connection timeout parameters for better reliability
                if (!query.contains("connectTimeout")) {
                    query += "&connectTimeout=10&socketTimeout=30";
                }
                
                // Build JDBC URL
                String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s?%s", host, port, database, query);
                
                System.out.println("Using Render DATABASE_URL - Host: " + host + ", Database: " + database);
                
                return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .driverClassName("org.postgresql.Driver")
                    .build();
            } catch (Exception e) {
                // If parsing fails, log error and fall back to default configuration
                System.err.println("Warning: Failed to parse DATABASE_URL, falling back to default configuration: " + e.getMessage());
                e.printStackTrace();
                return properties.initializeDataSourceBuilder().build();
            }
        }
        
        // Use default configuration from application.yml (for local development with docker-compose)
        System.out.println("Using local database configuration");
        return properties.initializeDataSourceBuilder().build();
    }
}

