// src/main/java/com/kaiburr/task1backend/Task1BackendApplication.java

package com.kaiburr.task1_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Task1BackendApplication {

    public static void main(String[] args) {
        // This static method runs the Spring Boot application.
        // It sets up the embedded server (like Tomcat), initializes the context, 
        // and starts listening for requests on the configured port (default is 8080).
        SpringApplication.run(Task1BackendApplication.class, args);
    }
}