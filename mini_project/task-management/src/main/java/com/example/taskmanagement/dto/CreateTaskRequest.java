package com.example.taskmanagement.dto;

public class CreateTaskRequest {

    private String title;
    private String assignedToUsername; // SD username

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
}
