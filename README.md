# KAIBURR Assessment - Task 1: Java Backend and REST API

This repository contains the solution for **Task 1** of the Kaiburr assessment. This application is a Java Spring Boot backend that provides a REST API for managing "Task" objects, which represent shell commands intended to be run in a Kubernetes environment. All Task objects are persisted in a MongoDB database.

## Prerequisites

To compile, run, and test this application, you must have the following installed:

  * **Java Development Kit (JDK):** Version 17 or higher.
  * **Maven:** Project build tool.
  * **MongoDB:** A running instance accessible at `localhost:27017` (can be run via Docker or as a local service).
  * **HTTP Client:** Postman, Insomnia, or cURL for testing the API endpoints.

## Build and Run Instructions

1.  **Clone the Repository:**
    ```bash
    git clone [your-repo-url]
    cd task1-backend
    ```
2.  **Start MongoDB:**
    (If using Docker, run this command to start the database):
    ```bash
    docker run -d --name mongo-task1 -p 27017:27017 mongo:latest
    ```
3.  **Configure:** Ensure your `src/main/resources/application.properties` file points to the correct MongoDB host and port (default is `localhost:27017`).
4.  **Build the Project:**
    ```bash
    mvn clean install
    ```
5.  **Run the Application:**
    ```bash
    mvn spring-boot:run
    ```
    The application will start on port `8080`.

## Task Data Models

The API manages two primary objects:

### 1\. Task

| Property | Type | Description |
| :--- | :--- | :--- |
| `id` | `String` | Task ID (Primary Key). |
| `name` | `String` | Task name. |
| `owner` | `String` | Task owner. |
| `command` | `String` | Shell command to be executed. |
| `taskExecutions` | `List<TaskExecution>` | List of past execution results. |

### 2\. TaskExecution

| Property | Type | Description |
| :--- | :--- | :--- |
| `startTime` | `Date` | Execution start date/time. |
| `endTime` | `Date` | Execution end date/time. |
| `output` | `String` | Command output (stdout/stderr). |

## REST API Endpoints

All endpoints use the base path `/tasks`.

| Method | Endpoint | Description | Status Codes |
| :--- | :--- | :--- | :--- |
| **GET** | `/tasks` | Returns all Task objects. | `200 OK` |
| **GET** | `/tasks?id={id}` | Returns a single Task by ID. | `200 OK`, `404 Not Found` |
| **GET** | `/tasks/findByName?name={string}` | Searches for tasks whose name contains the provided string (case-insensitive). | `200 OK`, `404 Not Found`  |
| **PUT** | `/tasks` | Creates a new Task or updates an existing one using the JSON body. Includes validation to reject unsafe/malicious shell commands. | `201 Created`, `400 Bad Request` |
| **PUT** | `/tasks/{id}/execute` | Executes the Task's shell command locally (Task 1 implementation). Stores the `TaskExecution` result in the Task object. | `200 OK`, `404 Not Found` |
| **DELETE** | `/tasks/{id}` | Deletes the Task object specified by ID. | `200 OK`, `404 Not Found` |

-----

## API Testing and Screenshots

The following screenshots confirm the functionality of all required endpoints, captured using Postman.

-----

**NOTE:** Each screenshot includes the current date/time and the username visible on the screen to validate the work was performed by the candidate.

### 1\. PUT /tasks (Create Task)

**Purpose:** Creates task `T002` with a simple `echo` command.

<img width="1454" height="589" alt="image" src="https://github.com/user-attachments/assets/e30ed391-37b6-438b-b94b-77725650d15a" />

-----

-----

### 2\. PUT /tasks/{id}/execute (Run Command)

**Purpose:** Executes the shell command (`hostname`) for task `T001` and stores the output.

<img width="1460" height="406" alt="image" src="https://github.com/user-attachments/assets/e551c538-ac03-4557-9153-b05a0859ab4b" />

-----

-----

### 3\. GET /tasks (Get All Tasks)

**Purpose:** Retrieves all tasks, confirming `T001` and `T002` are stored with their execution logs.

<img width="1452" height="483" alt="image" src="https://github.com/user-attachments/assets/79918e15-bb39-47b3-801c-dd8990f76c80" />

-----

-----

### 4\. GET /tasks/findByName (Search)

**Purpose:** Searches for tasks containing the string "host" in their name.

<img width="1455" height="494" alt="image" src="https://github.com/user-attachments/assets/7242c2d9-66d8-4a13-a335-d157eac4ba22" />

-----

-----

### 5\. DELETE /tasks/{id} (Delete Task)

**Purpose:** Deletes task `T002`.
<img width="1454" height="371" alt="image" src="https://github.com/user-attachments/assets/c2120559-15e6-4d33-9aa2-1cab0e7fbf13" />

-----
