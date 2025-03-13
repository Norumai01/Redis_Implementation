package com.norumai.redistest;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Objects;

@SpringBootApplication
public class RedisTestApplication {

    private static final Logger logger = LoggerFactory.getLogger(RedisTestApplication.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RedisTestApplication.class, args);
    }

    // Standard Redis with no polling, so will keep connecting/disconnecting every commands made.
//    @EventListener(ApplicationReadyEvent.class)
//    public void onApplicationReady() {
//        try {
//            logger.info("Testing Redis connection with Lettuce...");
//
//            stringRedisTemplate.opsForValue().set("test:key", "Hello from Spring Boot");
//
//            String test = stringRedisTemplate.opsForValue().get("test:key");
//            logger.debug("Retrieved test value: {}", test);
//
//            // Check connection
//            boolean isClosed = Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()).getConnection().isClosed();
//            logger.debug("Redis connection is closed: {}", isClosed ? "yes" : "no");
//
//            if (stringRedisTemplate.opsForValue().get("test:key") != null) {
//                logger.debug("Deleting test value: {}", test);
//                stringRedisTemplate.opsForValue().getAndDelete("test:key");
//            }
//
//            logger.info("Redis connection test completed.");
//        }
//        catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//    }

    // With Lettuce to start and end the connection.
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        RedisURI redisURI = RedisURI.builder()
                .withHost("localhost")
                .withPort(6379)
//                    .withAuthentication("user", "password") // If user or password exists.
                .build();

        RedisClient redisClient = RedisClient.create(redisURI);

        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            logger.info("Connected to Redis");

            RedisCommands<String, String> commands = connection.sync();

            commands.set("test:key", "Hello from Spring Boot");
            String value = commands.get("test:key");

            logger.debug("Value of test:key is {}", value);

            commands.del("test:key");
            logger.info("Deleted test:key");
        }
        catch (Exception e) {
            logger.error("Redis connection test failed:  {}", e.getMessage());
        }
        finally {
            redisClient.shutdown();
            logger.info("Redis shutdown.");
        }

        logger.info("Redis connection test completed.");
    }
}
