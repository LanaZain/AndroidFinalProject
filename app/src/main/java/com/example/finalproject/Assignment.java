package com.example.finalproject;

public class Assignment {
    private String title, description, dueDate, status;

    public Assignment(String title, String description, String dueDate, String status) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDueDate() { return dueDate; }
    public String getStatus() { return status; }
}
