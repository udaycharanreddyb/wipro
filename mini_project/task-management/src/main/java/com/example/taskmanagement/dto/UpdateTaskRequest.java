package com.example.taskmanagement.dto;

import com.example.taskmanagement.entity.TaskStatus;

public class UpdateTaskRequest {

    private String title;
    private String assignedToUsername;
    private TaskStatus status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public String getAssignedToUsername() {
        return assignedToUsername;
    }

    public void setAssignedToUsername(String assignedToUsername) {
        this.assignedToUsername = assignedToUsername;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) { this.status = status; }
}
