// src/main/java/com/kaiburr/task1_backend/controller/TaskController.java

package com.kaiburr.task1_backend.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.kaiburr.task1_backend.model.Task;
import com.kaiburr.task1_backend.model.TaskExecution;
import com.kaiburr.task1_backend.repository.TaskRepository;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * GET tasks: Return all tasks or a single task by ID[cite: 65, 66].
     * * @param id Optional task ID parameter.
     * @return List of tasks or a single task.
     */
    @GetMapping
    public ResponseEntity<Object> getTasks(@RequestParam(required = false) String id) {
        if (id != null) {
            // Return a single task by ID 
            Optional<Task> task = taskRepository.findById(id);
            if (task.isPresent()) {
                return ResponseEntity.ok(task.get());
            } else {
                // Return 404 if no such task 
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " not found.");
            }
        }
        // Return all tasks 
        List<Task> tasks = taskRepository.findAll();
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET (find) tasks by name[cite: 74, 75].
     * * @param name Name string to search for.
     * @return List of matching tasks.
     */
    @GetMapping("/findByName")
    public ResponseEntity<List<Task>> getTasksByName(@RequestParam String name) {
        // findByNameContainingIgnoreCase is defined in TaskRepository [cite: 74, 75]
        List<Task> tasks = taskRepository.findByNameContainingIgnoreCase(name);

        if (tasks.isEmpty()) {
            // Return 404 if nothing is found [cite: 76]
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tasks found with name containing: " + name);
        }
        return ResponseEntity.ok(tasks);
    }

    /**
     * PUT a task (Create/Update).
     * * @param task The task object in the request body.
     * @return The created or updated Task.
     */
    @PutMapping
    public ResponseEntity<Task> createOrUpdateTask(@RequestBody Task task) {
        // Basic command validation - a simple check for common malicious commands [cite: 68]
        if (task.getCommand() != null && task.getCommand().matches(".*(rm -rf|:\\(\\)\\{ :\\|& };).*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Command contains unsafe/malicious code.");
        }

        // save() handles both creation (new ID) and update (existing ID)
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    /**
     * DELETE a task.
     * * @param id The task ID to delete.
     * @return 204 No Content on successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        if (!taskRepository.existsById(id)) {
            // Throw 404 if the task doesn't exist
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " not found.");
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PUT a TaskExecution (Run a shell command and store execution)[cite: 77, 78].
     * NOTE: This is the simplified implementation for Task 1, running locally. 
     * Task 2 requires programmatic Kubernetes pod creation[cite: 77, 100].
     * * @param id The task ID.
     * @return The updated Task object.
     */
    @PutMapping("/{id}/execute")
    public ResponseEntity<Task> executeTask(@PathVariable String id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " not found."));

        // 1. Prepare TaskExecution
        TaskExecution execution = new TaskExecution();
        execution.setStartTime(Instant.now());

        String commandOutput = "Execution Not Possible/Failed"; // Default output

        // 2. Execute the shell command locally (Task 1 requirement)
        try {
            String[] commandParts = task.getCommand().split("\\s+");
            Process process = new ProcessBuilder(commandParts).start();

            // Wait for command to finish and read output
            if (process.waitFor() == 0) { // Check for successful exit code (0)
                commandOutput = new String(process.getInputStream().readAllBytes()).trim();
            } else {
                // Read error stream for non-zero exit
                commandOutput = "Error (Exit Code " + process.exitValue() + "): " + new String(process.getErrorStream().readAllBytes()).trim();
            }

        } catch (IOException e) {
            // Handle issues like command not found
            commandOutput = "IO Error running command: " + e.getMessage();
        } catch (InterruptedException e) {
            // CRITICAL STEP 1: Restore the interrupted status
            Thread.currentThread().interrupt();
            // CRITICAL STEP 2: Abort or throw a Spring-friendly exception
            // The thread was interrupted, usually meaning the system is shutting down or
            // being cancelled. We should stop processing immediately.
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Task execution interrupted.", e);
        }

        // 3. Update Task object with results
        execution.setEndTime(Instant.now());
        execution.setOutput(commandOutput);

        // Ensure the list is initialized before adding
        if (task.getTaskExecutions() == null) {
            task.setTaskExecutions(new java.util.ArrayList<>());
        }
        task.getTaskExecutions().add(execution);

        // 4. Save the updated task
        Task updatedTask = taskRepository.save(task);
        return ResponseEntity.ok(updatedTask);
    }
}