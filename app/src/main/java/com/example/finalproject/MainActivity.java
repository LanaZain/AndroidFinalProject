package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast; // Import Toast for placeholders

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView; // Import MaterialCardView

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView tvName, tvGradeSection;
    private ImageView profileImage;

    String studentId = "1"; // Fixed student ID for testing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the profile views
        tvName = findViewById(R.id.textName);
        tvGradeSection = findViewById(R.id.textGradeSection);
        profileImage = findViewById(R.id.imageProfile);

        // Fetch the student's profile data
        fetchStudentProfile();
        setupCardListeners();
    }

    private void setupCardListeners() {
        // Get references to each card from the layout
        MaterialCardView cardAssignments = findViewById(R.id.cardAssignments);
        MaterialCardView cardGrades = findViewById(R.id.cardGrades);
        MaterialCardView cardSchedule = findViewById(R.id.cardSchedule);
        MaterialCardView cardExams = findViewById(R.id.cardExams);

        cardAssignments.setOnClickListener(v -> {
            // This correctly launches your AssignmentsActivity
            Intent intent = new Intent(MainActivity.this, AssignmentsActivity.class);
            startActivity(intent);
        });

        cardGrades.setOnClickListener(v -> {
             Intent intent = new Intent(MainActivity.this, GradesActivity.class);
             startActivity(intent);
            Toast.makeText(this, "Grades card clicked!", Toast.LENGTH_SHORT).show();
        });

        cardSchedule.setOnClickListener(v -> {
             Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
             startActivity(intent);
            Toast.makeText(this, "Schedule card clicked!", Toast.LENGTH_SHORT).show();
        });

        cardExams.setOnClickListener(v -> {
            // TODO: Create and launch ExamsActivity
            // Intent intent = new Intent(MainActivity.this, ExamsActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "Exams card clicked!", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchStudentProfile() {
        new Thread(() -> {
            try {
                String baseUrl = getString(R.string.ip);
                String urlString = baseUrl + "/mobileProject/get_student_profile.php?student_id=" + studentId;

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONObject json = new JSONObject(response.toString());

                    final String name = json.getString("name");
                    final String grade = json.getString("grade");
                    final String section = json.getString("section");
                    final String photoUrl = json.getString("photo_url");

                    runOnUiThread(() -> {
                        tvName.setText(name);
                        tvGradeSection.setText("Class\n" + grade + " " + section);

                        Glide.with(MainActivity.this)
                                .load(photoUrl)
                                .circleCrop()
                                .placeholder(R.drawable.circle_bg)
                                .error(R.drawable.circle_bg)
                                .into(profileImage);
                    });
                } else {
                    // Handle server errors
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}