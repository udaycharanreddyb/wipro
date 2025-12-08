package com.example.taskmanagement.dto;

import com.example.taskmanagement.entity.TaskStatus;

public class UpdateTaskStatusRequest {

    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) { this.status = status; }
}
