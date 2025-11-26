package com.nextmatch.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class NextMatchBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NextMatchBackendApplication.class, args);
    }
}
