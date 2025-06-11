package com.example.finalproject;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView profileName, infoStudentId, infoGradeSection, infoMajor,
            infoEmail, infoPhone, infoBirthdate, infoEnrollDate, infoAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Setup Toolbar with back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Initialize all views
        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        infoStudentId = findViewById(R.id.info_student_id);
        infoGradeSection = findViewById(R.id.info_grade_section);
        infoMajor = findViewById(R.id.info_major);
        infoEmail = findViewById(R.id.info_email);
        infoPhone = findViewById(R.id.info_phone);
        infoBirthdate = findViewById(R.id.info_birthdate);
        infoEnrollDate = findViewById(R.id.info_enroll_date);
        infoAddress = findViewById(R.id.info_address);

        // Get the student ID passed from MainActivity
        String studentId = getIntent().getStringExtra("STUDENT_ID");
        if (studentId == null || studentId.isEmpty()) {
            Toast.makeText(this, "Error: Student ID not provided.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        fetchProfileData(studentId);
    }

    private void fetchProfileData(String studentId) {
        new Thread(() -> {
            try {
                String urlString = "http://10.0.2.2/mobileProject/get_student_profile.php?student_id=" + studentId;
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    JSONObject json = new JSONObject(response.toString());

                    // Parse all the new fields
                    final String name = json.getString("name");
                    final String studentNumber = json.getString("student_number");
                    final String grade = json.getString("grade");
                    final String section = json.getString("section");
                    final String major = json.getString("major");
                    final String email = json.getString("email");
                    final String phone = json.getString("phone");
                    final String birthdate = json.getString("birth_date_formatted");
                    final String enrollDate = json.getString("enrollment_date_formatted");
                    final String address = json.getString("address");
                    final String photoUrl = json.getString("photo_url");

                    // Update UI on the main thread
                    runOnUiThread(() -> {
                        profileName.setText(name);
                        infoStudentId.setText("Student ID: " + studentNumber);
                        infoGradeSection.setText("Class: " + grade + " " + section);
                        infoMajor.setText("Major: " + major);
                        infoEmail.setText("Email: " + email);
                        infoPhone.setText("Phone: " + phone);
                        infoBirthdate.setText("Birth Date: " + birthdate);
                        infoEnrollDate.setText("Enrolled: " + enrollDate);
                        infoAddress.setText("Address: " + address);

                        Glide.with(this)
                                .load(photoUrl)
                                .circleCrop()
                                .placeholder(R.drawable.circle_bg)
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