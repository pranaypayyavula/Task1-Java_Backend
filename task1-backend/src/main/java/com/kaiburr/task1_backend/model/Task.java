// src/main/java/com/kaiburr/task1backend/model/Task.java

package com.kaiburr.task1_backend.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks") // Maps this class to a MongoDB collection named 'tasks' [cite: 79]
public class Task {

    @Id // Designates this field as the primary key/document ID in MongoDB [cite: 34]
    private String id;

    // task name, String [cite: 35]
    private String name;

    // task owner, String [cite: 36]
    private String owner;

    // shell command to be run, String [cite: 37]
    private String command;

    // List<TaskExecuton> [cite: 38]
    private List<TaskExecution> taskExecutions;
}