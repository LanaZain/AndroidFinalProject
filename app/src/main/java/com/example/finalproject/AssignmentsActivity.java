package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AssignmentsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AssignmentAdapter adapter;
    ArrayList<Assignment> assignmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        // Set header title
        TextView headerTitle = findViewById(R.id.headerTitle);
        headerTitle.setText(R.string.assignments_head);
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());


        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        assignmentList = new ArrayList<>();
        assignmentList.add(new Assignment("Math Homework–Mathematics", "Quadratic equations", "May 15, 2024, 11:59 PM", "Graded"));
        assignmentList.add(new Assignment("English Homework–English", "Simple Past", "May 14, 2024, 11:59 PM", "Pending"));
        assignmentList.add(new Assignment("History Assignment–History", "World War 2", "May 20, 2024, 11:59 PM", "Submitted"));
        assignmentList.add(new Assignment("Chemistry Homework–Chemistry", "Kinetic and Solutions", "May 3, 2024, 11:59 PM", "Late"));

        // Handle click event
        AssignmentAdapter.OnItemClickListener listener = assignment -> {
            Intent intent = new Intent(AssignmentsActivity.this, SubmitHomeworkActivity.class);
            intent.putExtra("title", assignment.getTitle());
            intent.putExtra("description", assignment.getDescription());
            intent.putExtra("dueDate", assignment.getDueDate());
            intent.putExtra("status", assignment.getStatus());
            startActivity(intent);
        };

        adapter = new AssignmentAdapter(this, assignmentList, listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
