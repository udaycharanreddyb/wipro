package com.example.taskmanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.taskmanagement.dto.CreateTaskRequest;
import com.example.taskmanagement.dto.TaskResponse;
import com.example.taskmanagement.dto.UpdateTaskRequest;
import com.example.taskmanagement.dto.UpdateTaskStatusRequest;
import com.example.taskmanagement.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // TEAM_LEAD: create a task and assign to SD
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    // TEAM_LEAD: all tasks
    // SD: only his tasks
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks() {
        return ResponseEntity.ok(taskService.getTasksForCurrentUser());
    }

    // TEAM_LEAD: full update
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    // SD / TEAM_LEAD: update only status
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Long id,
            @RequestBody UpdateTaskStatusRequest request) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, request));
    }

    // TEAM_LEAD: delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
