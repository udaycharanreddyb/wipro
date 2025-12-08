package com.example.taskmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    // username of SD assigned to this task
    private String assignedTo;      // e.g. "sd1"

    // username of Team Lead who created this task
    private String createdBy;       // e.g. "lead1"

    public Task() {
    }

    // getters & setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) { this.status = status; }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
