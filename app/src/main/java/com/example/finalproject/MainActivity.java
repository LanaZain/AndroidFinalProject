package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView tvName, tvGradeSection;
    private ImageView profileImage;
    private Button logoutButton;

    String studentId = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        AuthManager authManager = new AuthManager(this);
        if (! authManager.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        logoutButton = findViewById(R.id.logoutButton4);

        logoutButton.setOnClickListener(v -> {
            authManager.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        // Initialize bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set navigation item selected listener inside onCreate
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Already on home
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("STUDENT_ID", studentId);
                startActivity(intent);
                return true;
            }

            return false;
        });


        // Initialize the profile views
        tvName = findViewById(R.id.textName);
        tvGradeSection = findViewById(R.id.textGradeSection);
        profileImage = findViewById(R.id.imageProfile);

        fetchStudentProfile();
        setupCardListeners();
    }

    private void setupCardListeners() {
        MaterialCardView cardAssignments = findViewById(R.id.cardAssignments);
        MaterialCardView cardGrades = findViewById(R.id.cardGrades);
        MaterialCardView cardSchedule = findViewById(R.id.cardSchedule);
        MaterialCardView cardExams = findViewById(R.id.cardExams);

        cardAssignments.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AssignmentsActivity.class);
            startActivity(intent);
        });

        cardGrades.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GradesActivity.class);
            startActivity(intent);
        });

        cardSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            startActivity(intent);
        });

        cardExams.setOnClickListener(v -> {
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
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
