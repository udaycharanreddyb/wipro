package com.example.taskmanagement.dto;

import com.example.taskmanagement.entity.TaskStatus;

public class TaskResponse {

    private Long id;
    private String title;
    private TaskStatus status;
    private String assignedTo;
    private String createdBy;

    public TaskResponse() {}

    public TaskResponse(Long id, String title,
                        TaskStatus status, String assignedTo, String createdBy) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.assignedTo = assignedTo;
        this.createdBy = createdBy;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public TaskStatus getStatus() { return status; }

    public void setStatus(TaskStatus status) { this.status = status; }

    public String getAssignedTo() { return assignedTo; }

    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public String getCreatedBy() { return createdBy; }

    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
