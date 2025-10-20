// src/main/java/com/kaiburr/task1backend/repository/TaskRepository.java

package com.kaiburr.task1_backend.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.kaiburr.task1_backend.model.Task;

public interface TaskRepository extends MongoRepository<Task, String> {

    /**
     * Finds tasks whose name contains the given string (case-insensitive)[cite: 74, 75].
     * Spring Data automatically generates the implementation for this method name.
     */
    List<Task> findByNameContainingIgnoreCase(String name);
}