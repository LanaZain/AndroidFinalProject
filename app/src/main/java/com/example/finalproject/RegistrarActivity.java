package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistrarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button buttonaddsubject= findViewById(R.id.buttonViewClassSchedule);
        Button addStudentBtn = findViewById(R.id.buttonAddStudent);
        Button addSchBtn = findViewById(R.id.buttonAddStch);
        Button viewStudentsBtn = findViewById(R.id.buttonViewStudents);
        Button viewteachersch= findViewById(R.id.buttonViewTeacherSchedule);
        addStudentBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddStudentActivity.class);
            startActivity(intent);
        });

        addSchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddScheduleActivity.class);
            startActivity(intent);
        });

        viewStudentsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewStudentsActivity.class);
            startActivity(intent);
        });Button addTeacherBtn = findViewById(R.id.buttonAddTeacher);

        addTeacherBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTeacherActivity.class);
            startActivity(intent);
        });

        buttonaddsubject .setOnClickListener(v -> {
            Intent intent = new Intent(this,AddSubjectActivity.class);
            startActivity(intent);
        });

    }
}
