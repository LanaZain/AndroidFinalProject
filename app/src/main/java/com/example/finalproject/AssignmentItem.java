package com.example.finalproject;

// This class is a container to hold all the data needed for one row in the RecyclerView
public class AssignmentItem {

    private Assignment assignment;
    private String submissionStatus;

    public AssignmentItem(Assignment assignment, String submissionStatus) {
        this.assignment = assignment;
        this.submissionStatus = submissionStatus;
    }

    // Getters to access the contained data
    public Assignment getAssignment() {
        return assignment;
    }

    public String getSubmissionStatus() {
        return submissionStatus;
    }
}