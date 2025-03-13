# Redis Test Application

A simple Spring Boot application to test Redis connectivity using Lettuce client.

## Overview

This application demonstrates how to:
- Connect to a Redis instance using Spring Boot and Lettuce client
- Perform basic Redis operations (SET, GET, DELETE)
- Handle connections properly with try-with-resources

## Prerequisites

- Java 11 or higher
- Maven 3.6+ or Gradle 7+
- Redis server

## Installing Redis

### Windows

Redis doesn't officially support Windows, but you can use:

1. **Windows Subsystem for Linux (WSL)** (Recommended):
   ```
   # Install WSL if not already installed
   wsl --install

   # In your WSL terminal
   sudo apt update
   sudo apt install redis-server
   sudo service redis-server start
   ```

2. **Docker** (Alternative):
   ```
   docker run --name redis -p 6379:6379 -d redis
   ```

### macOS

Using Homebrew:
```
brew install redis
brew services start redis
```

### Linux (Ubuntu/Debian)

```
sudo apt update
sudo apt install redis-server
sudo systemctl start redis-server
```

### Verify Installation

To verify Redis is running:
```
redis-cli ping
```
Should return: `PONG`

## Running the Application

### Clone the repository

```
git clone https://github.com/yourusername/redis-test.git
cd redis-test
```

### Configure Redis Connection (if needed)

By default, the application connects to Redis on `localhost:6379` without authentication. If your Redis setup is different, modify the Redis connection settings in `RedisTestApplication.java`:

```java
RedisURI redisURI = RedisURI.builder()
        .withHost("your-redis-host")  // Change from localhost if needed
        .withPort(6379)               // Change if using different port
        // .withAuthentication("user", "password") // Uncomment if using authentication
        .build();
```

### Run the application

With Maven:
```
mvn spring-boot:run
```

With Gradle:
```
./gradlew bootRun
```

## Expected Output

When successfully connected to Redis, you should see log outputs similar to:

```
INFO: Connected to Redis
DEBUG: Value of test:key is Hello from Spring Boot
INFO: Deleted test:key
INFO: Redis shutdown.
INFO: Redis connection test completed.
```

## Project Structure

- `RedisTestApplication.java` - Main application class with Redis connectivity test
- The application includes two implementation approaches (one commented out):
  - Standard Spring Boot `StringRedisTemplate` approach
  - Direct Lettuce client connection with proper connection handling

## Dependencies

- Spring Boot
- Spring Data Redis
- Lettuce Redis client
- SLF4J for logging

## Troubleshooting

1. **Connection Refused**
   - Ensure Redis is running: `redis-cli ping`
   - Check if Redis is running on the expected host/port

2. **Authentication Failed**
   - If your Redis requires authentication, uncomment and update the authentication line

3. **Redis Command Errors**
   - Check Redis logs for any error messages
   - Verify that your Redis server version is compatible with the client

