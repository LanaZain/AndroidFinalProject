package com.example.finalproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewStudentsActivity extends AppCompatActivity {

    private Button[] classButtons = new Button[12];
    private int[] buttonIds = {
            R.id.btnClass1, R.id.btnClass2, R.id.btnClass3, R.id.btnClass4,
            R.id.btnClass5, R.id.btnClass6, R.id.btnClass7, R.id.btnClass8,
            R.id.btnClass9, R.id.btnClass10, R.id.btnClass11, R.id.btnClass12
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        for (int i = 0; i < buttonIds.length; i++) {
            final int classId = i + 1;
            classButtons[i] = findViewById(buttonIds[i]);

            classButtons[i].setOnClickListener(v -> showOptionsDialog(classId));
        }
    }

    private void showOptionsDialog(int classId) {
        // تأكد من قيمة classId بالعرض على الشاشة
        Toast.makeText(this, "Selected Class ID: " + classId, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("اختر الإجراء");

        String[] options = {"عرض الطلاب", "عرض الجدول"};

        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                openStudentsList(classId);
            } else if (which == 1) {
                openSchedule(classId);
            }
        });

        builder.show();
    }

    private void openStudentsList(int classId) {
        Intent intent = new Intent(this, StudentsListActivity.class);
        intent.putExtra("class_id", classId);
        startActivity(intent);
    }

    private void openSchedule(int classId) {
        Intent intent = new Intent(this, ViewScheduleActivity.class);
        intent.putExtra("class_id", classId);
        startActivity(intent);
    }
}
