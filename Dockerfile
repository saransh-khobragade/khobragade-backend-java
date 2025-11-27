# Use OpenJDK 17 as base image
FROM gradle:8.5-jdk17 AS build

# Set working directory
WORKDIR /app

# Copy gradle files first for better caching
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY gradlew ./

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src ./src

# Build the application
RUN ./gradlew build -x test --no-daemon

# Create a new stage for runtime
FROM eclipse-temurin:17-jre

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 