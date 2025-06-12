package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddSubjectActivity extends AppCompatActivity {

    EditText etSubjectName, etSubjectCode, etDescription, etCreditHours, etDepartment;
    Button btnAddSubject, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        // ربط العناصر من XML
        etSubjectName = findViewById(R.id.etSubjectName);
        etSubjectCode = findViewById(R.id.etSubjectCode);
        etDescription = findViewById(R.id.etDescription);
        etCreditHours = findViewById(R.id.etCreditHours);
        etDepartment = findViewById(R.id.etDepartment);
        btnAddSubject = findViewById(R.id.btnAddSubject);
        logoutButton = findViewById(R.id.logoutButton3);

        AuthManager authManager = new AuthManager(this);

        logoutButton.setOnClickListener(v -> {
            authManager.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        btnAddSubject.setOnClickListener(v -> {
            String name = etSubjectName.getText().toString().trim();
            String code = etSubjectCode.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();
            String creditStr = etCreditHours.getText().toString().trim();
            String dept = etDepartment.getText().toString().trim();

            if (name.isEmpty() || code.isEmpty() || creditStr.isEmpty()) {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int credit = Integer.parseInt(creditStr);
            addSubject(name, code, desc, credit, dept);
        });
    }

    private void addSubject(String name, String code, String desc, int credit, String dept) {
        String url =  getString(R.string.ip)+ "shaimaa//add_subject.php"; // استبدله بعنوان السيرفر

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equalsIgnoreCase("Success")) {
                        Toast.makeText(this, "Subject added successfully", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(this, "Failed: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("subject_name", name);
                params.put("subject_code", code);
                params.put("description", desc);
                params.put("credit_hours", String.valueOf(credit));
                params.put("department", dept);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void clearFields() {
        etSubjectName.setText("");
        etSubjectCode.setText("");
        etDescription.setText("");
        etCreditHours.setText("");
        etDepartment.setText("");
    }
}
