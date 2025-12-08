package com.example.taskmanagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.taskmanagement.dto.CreateTaskRequest;
import com.example.taskmanagement.dto.TaskResponse;
import com.example.taskmanagement.dto.UpdateTaskRequest;
import com.example.taskmanagement.dto.UpdateTaskStatusRequest;
import com.example.taskmanagement.entity.Role;
import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.entity.TaskStatus;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private String getCurrentUsername() {
        return getAuth().getName();
    }

    private boolean currentUserIs(Role role) {
        return getAuth().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role.name()));
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getStatus(),
                task.getAssignedTo(),
                task.getCreatedBy()
        );
    }

    // TEAM LEAD: create task & assign to SD
    public TaskResponse createTask(CreateTaskRequest request) {

        if (!currentUserIs(Role.TEAM_LEAD)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only TEAM_LEAD can create tasks");
        }

        var user = userRepository.findByUsername(request.getAssignedToUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Assigned user not found"));

        if (user.getRole() != Role.SD) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Assigned user must have SD role");
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        // description not used anymore
        task.setStatus(TaskStatus.PENDING);
        task.setAssignedTo(request.getAssignedToUsername());
        task.setCreatedBy(getCurrentUsername());

        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    public List<TaskResponse> getTasksForCurrentUser() {

        String username = getCurrentUsername();

        if (currentUserIs(Role.TEAM_LEAD)) {
            return taskRepository.findAll()
                    .stream().map(this::toResponse)
                    .collect(Collectors.toList());
        } else {
            return taskRepository.findByAssignedTo(username)
                    .stream().map(this::toResponse)
                    .collect(Collectors.toList());
        }
    }

    // TEAM_LEAD: full update
    public TaskResponse updateTask(Long taskId, UpdateTaskRequest request) {

        if (!currentUserIs(Role.TEAM_LEAD)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only TEAM_LEAD can fully update tasks");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Task not found"));

        task.setTitle(request.getTitle());
        // description not used
        task.setStatus(request.getStatus());
        task.setAssignedTo(request.getAssignedToUsername());

        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    public TaskResponse updateTaskStatus(Long taskId, UpdateTaskStatusRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Task not found"));

        String username = getCurrentUsername();

        if (currentUserIs(Role.SD) && !username.equals(task.getAssignedTo())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "SD can only update their own tasks");
        }

        task.setStatus(request.getStatus());
        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    public void deleteTask(Long taskId) {

        if (!currentUserIs(Role.TEAM_LEAD)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only TEAM_LEAD can delete tasks");
        }

        if (!taskRepository.existsById(taskId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        taskRepository.deleteById(taskId);
    }
}
