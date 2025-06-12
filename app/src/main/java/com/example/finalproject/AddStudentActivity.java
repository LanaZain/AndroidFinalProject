package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddStudentActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etEmail, etFirstName, etLastName, etPhoneNumber;
    EditText etStudentNumber, etAddress, etBirthDate, etMajor,
            etEnrollmentDate, etGraduationDate, etCurrentSemester, etClassId;
    Button btnAddStudent;

//    String insertUrl =  getString(R.string.ip)+ "shaimaa/add_student.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        // ربط عناصر المستخدم
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);

        // ربط عناصر الطالب
        etStudentNumber = findViewById(R.id.etStudentNumber);
        etAddress = findViewById(R.id.etAddress);
        etBirthDate = findViewById(R.id.etBirthDate);
        etMajor = findViewById(R.id.etMajor);
        etEnrollmentDate = findViewById(R.id.etEnrollmentDate);
        etGraduationDate = findViewById(R.id.etGraduationDate);
        etCurrentSemester = findViewById(R.id.etCurrentSemester);
        etClassId = findViewById(R.id.etClassId);

        btnAddStudent = findViewById(R.id.btnAddStudent);

        btnAddStudent.setOnClickListener(v -> {
            try {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String phoneNumber = etPhoneNumber.getText().toString().trim();

                String studentNumber = etStudentNumber.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                String birthDate = etBirthDate.getText().toString().trim();
                String major = etMajor.getText().toString().trim();
                String enrollmentDate = etEnrollmentDate.getText().toString().trim();
                String graduationDate = etGraduationDate.getText().toString().trim();
                int currentSemester = Integer.parseInt(etCurrentSemester.getText().toString().trim());
                int classId = Integer.parseInt(etClassId.getText().toString().trim());

                sendStudentToServer(username, password, email, firstName, lastName, phoneNumber,
                        studentNumber, address, birthDate, major, enrollmentDate, graduationDate, currentSemester, classId);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error in input: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("AddStudent", "Input error", e);
            }
        });
    }

    private void sendStudentToServer(String username, String password, String email,
                                     String firstName, String lastName, String phoneNumber,
                                     String studentNumber, String address, String birthDate,
                                     String major, String enrollmentDate, String graduationDate,
                                     int currentSemester, int classId) {

        RequestQueue queue = Volley.newRequestQueue(this);

//        StringRequest stringRequest = new StringRequest(Request.Method.POST, insertUrl,
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.ip)+ "shaimaa/add_student.php",
                response -> Toast.makeText(getApplicationContext(), "Response: " + response, Toast.LENGTH_LONG).show(),
                error -> {
                    Toast.makeText(getApplicationContext(), "Volley error: " + error.toString(), Toast.LENGTH_LONG).show();
//                    Log.e("AddStudent", "Volley error", error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("phone_number", phoneNumber);
                params.put("student_number", studentNumber);
                params.put("address", address);
                params.put("birth_date", birthDate);
                params.put("major", major);
                params.put("enrollment_date", enrollmentDate);
                params.put("graduation_date", graduationDate);
                params.put("current_semester", String.valueOf(currentSemester));
                params.put("class_id", String.valueOf(classId));
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
