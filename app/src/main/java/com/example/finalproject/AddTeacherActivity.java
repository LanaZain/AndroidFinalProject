package com.example.finalproject;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.*;

import java.util.HashMap;
import java.util.Map;

public class AddTeacherActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etEmail, etFirstName, etLastName, etPhone;
    EditText etTeacherNumber, etDepartment, etHireDate, etSpecialization;
    Button btnAddTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        // ربط العناصر
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);

        etTeacherNumber = findViewById(R.id.etTeacherNumber);
        etDepartment = findViewById(R.id.etDepartment);
        etHireDate = findViewById(R.id.etHireDate);
        etSpecialization = findViewById(R.id.etSpecialization);

        btnAddTeacher = findViewById(R.id.btnAddTeacher);

        btnAddTeacher.setOnClickListener(v -> {
            addTeacherToServer();
        });
    }

    private void addTeacherToServer() {
        String url =  getString(R.string.ip)+ "shaimaa//add_teacher.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equalsIgnoreCase("Success")) {
                        Toast.makeText(this, "Teacher added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("username", etUsername.getText().toString());
                map.put("password", etPassword.getText().toString());
                map.put("email", etEmail.getText().toString());
                map.put("first_name", etFirstName.getText().toString());
                map.put("last_name", etLastName.getText().toString());
                map.put("phone_number", etPhone.getText().toString());
                map.put("teacher_number", etTeacherNumber.getText().toString());
                map.put("department", etDepartment.getText().toString());
                map.put("hire_date", etHireDate.getText().toString());
                map.put("specialization", etSpecialization.getText().toString());
                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
