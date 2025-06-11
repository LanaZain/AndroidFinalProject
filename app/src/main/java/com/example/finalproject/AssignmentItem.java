package com.example.finalproject;

public class AssignmentItem {

    private Assignment assignment;
    private String submissionStatus;

    public AssignmentItem(Assignment assignment, String submissionStatus) {
        this.assignment = assignment;
        this.submissionStatus = submissionStatus;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public String getSubmissionStatus() {
        return submissionStatus;
    }
}