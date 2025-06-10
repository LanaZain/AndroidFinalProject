package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AssignmentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AssignmentAdapter adapter;
    // --- CHANGE 1: The list is now of type AssignmentItem ---
    private ArrayList<AssignmentItem> assignmentItemList;

    private int currentStudentId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        TextView headerTitle = findViewById(R.id.headerTitle);
        headerTitle.setText(R.string.assignments_head);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerView);
        // --- CHANGE 2: Initialize the new list type ---
        assignmentItemList = new ArrayList<>();

        // --- CHANGE 3: The adapter's click listener now provides an AssignmentItem ---
        adapter = new AssignmentAdapter(this, assignmentItemList, item -> {
            Intent intent = new Intent(AssignmentsActivity.this, SubmitHomeworkActivity.class);
            Assignment assignment = item.getAssignment(); // Extract the assignment

            intent.putExtra("assignment_id", assignment.getId());
            intent.putExtra("title", assignment.getTitle());
            intent.putExtra("description", assignment.getDescription());
            intent.putExtra("dueDate", assignment.getDueDate());
            intent.putExtra("status", item.getSubmissionStatus());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchAssignments();
    }

    private void fetchAssignments() {
        String url = "http://10.0.2.2/mobileProject/assignments.php?action=list_assignments_for_student&student_id=" + currentStudentId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d("API_RESPONSE", response.toString());

                        if (response.getBoolean("success")) {
                            JSONArray data = response.getJSONArray("data");
                            assignmentItemList.clear(); // Clear the list before adding new items

                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);

                                // Format the due date
                                String formattedDate = "No due date";
                                try {
                                    Date dueDate = inputFormat.parse(obj.getString("due_date"));
                                    formattedDate = "Due: " + outputFormat.format(dueDate);
                                } catch (ParseException | JSONException e) {
                                    Log.e("DATE_ERROR", "Could not parse date: " + obj.optString("due_date"));
                                }

                                // Format the status for display
                                String status = obj.getString("submission_status");
                                String displayStatus = "Unknown";
                                if (status != null && !status.isEmpty()) {
                                    String[] words = status.replace("_", " ").split(" ");
                                    StringBuilder sb = new StringBuilder();
                                    for(String word : words) {
                                        sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
                                    }
                                    displayStatus = sb.toString().trim();
                                }

                                // --- CHANGE 4: Create the two separate objects ---
                                // 1. Create the pure Assignment object
                                Assignment assignment = new Assignment(
                                        obj.getInt("assignment_id"),
                                        obj.getString("title"),
                                        obj.getString("description"),
                                        formattedDate
                                );

                                // 2. Create the AssignmentItem wrapper and add it to the list
                                assignmentItemList.add(new AssignmentItem(assignment, displayStatus));
                            }

                            adapter.notifyDataSetChanged(); // Refresh the RecyclerView
                            if(assignmentItemList.isEmpty()) {
                                Toast.makeText(this, response.optString("message", "No assignments found"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "JSON parsing error: " + e.getMessage());
                        Toast.makeText(this, "There was a problem reading the data.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                });

        Volley.newRequestQueue(this).add(request);
    }
}