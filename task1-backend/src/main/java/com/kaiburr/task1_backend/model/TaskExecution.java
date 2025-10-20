// src/main/java/com/kaiburr/task1backend/model/TaskExecution.java

package com.kaiburr.task1_backend.model;

import java.time.Instant;

// Using Lombok annotations to automatically generate getters, setters, and constructors
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskExecution {

    // execution start date/time, Date [cite: 40]
    private Instant startTime;

    // execution end date/time, Date [cite: 40]
    private Instant endTime;

    // command output, String [cite: 41]
    private String output;
}