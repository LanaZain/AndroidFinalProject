package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddScheduleActivity extends AppCompatActivity {

    Spinner teacherSpinner, subjectSpinner, classSpinner, daySpinner;
    EditText startTimeEditText, endTimeEditText;
    Button submitButton;

    ArrayList<Teacher> teacherList = new ArrayList<>();
    ArrayList<Subject> subjectList = new ArrayList<>();
    ArrayList<SchoolClass> classList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        teacherSpinner = findViewById(R.id.spinner_teacher);
        subjectSpinner = findViewById(R.id.spinner_subject);
        classSpinner = findViewById(R.id.spinner_class);
        daySpinner = findViewById(R.id.spinner_day);
        startTimeEditText = findViewById(R.id.edit_start_time);
        endTimeEditText = findViewById(R.id.edit_end_time);
        submitButton = findViewById(R.id.button_submit);

        setupDaySpinner();

        loadSubjects(); // تحميل المواد أولاً
        loadClasses();  // تحميل الصفوف

        submitButton.setOnClickListener(view -> submitSchedule());
    }

    private void setupDaySpinner() {
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);
    }

    private void loadSubjects() {
        String url =  getString(R.string.ip)+ "shaimaa/get_subjects.php";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        subjectList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Subject subject = new Subject(
                                    obj.getInt("subject_id"),
                                    obj.getString("subject_name"));
                            subjectList.add(subject);
                        }
                        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, subjectList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subjectSpinner.setAdapter(adapter);

                        // عند اختيار مادة يتم تحميل المعلمين المختصين
                        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Subject selectedSubject = subjectList.get(position);
                                Log.d("AddScheduleActivity", "Selected subject: " + selectedSubject.name + ", id: " + selectedSubject.id);
                                loadTeachers(selectedSubject.id);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                teacherList.clear();
                                teacherSpinner.setAdapter(null);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "JSON Error (subjects)", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error loading subjects", Toast.LENGTH_SHORT).show();
                    Log.e("AddScheduleActivity", "Error loading subjects: " + error.toString());
                }
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void loadTeachers(int subjectId) {
        if (subjectId <= 0) {
            Log.e("AddScheduleActivity", "Invalid subjectId: " + subjectId);
            teacherList.clear();
            teacherSpinner.setAdapter(null);
            return;
        }

        String url =  getString(R.string.ip)+ "shaimaa/get_teachers_by_subject.php?subject_id=" + subjectId;
        Log.d("AddScheduleActivity", "Request URL: " + url);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        teacherList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Teacher teacher = new Teacher(
                                    obj.getInt("teacher_id"),
                                    obj.getString("first_name") + " " + obj.getString("last_name"));
                            teacherList.add(teacher);
                        }
                        ArrayAdapter<Teacher> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, teacherList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        teacherSpinner.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "JSON Error (teachers)", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error loading teachers", Toast.LENGTH_SHORT).show();
                    Log.e("AddScheduleActivity", "Error loading teachers: " + error.toString());
                }
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void loadClasses() {
        String url =  getString(R.string.ip)+ "shaimaa/get_classes.php";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        classList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            SchoolClass schoolClass = new SchoolClass(
                                    obj.getInt("class_id"),
                                    obj.getString("class_name"));
                            classList.add(schoolClass);
                        }
                        ArrayAdapter<SchoolClass> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, classList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        classSpinner.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "JSON Error (classes)", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error loading classes", Toast.LENGTH_SHORT).show();
                    Log.e("AddScheduleActivity", "Error loading classes: " + error.toString());
                }
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void submitSchedule() {
        Teacher teacher = (Teacher) teacherSpinner.getSelectedItem();
        Subject subject = (Subject) subjectSpinner.getSelectedItem();
        SchoolClass schoolClass = (SchoolClass) classSpinner.getSelectedItem();
        String day = daySpinner.getSelectedItem().toString();
        String start = startTimeEditText.getText().toString().trim();
        String end = endTimeEditText.getText().toString().trim();

        if (teacher == null || subject == null || schoolClass == null || start.isEmpty() || end.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String url =  getString(R.string.ip)+ "shaimaa/add_schedule.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(this, "Schedule added successfully", Toast.LENGTH_SHORT).show();
                        finish(); // ترجع للخلف بعد الإضافة
                    } else if (response.trim().equals("conflict")) {
                        Toast.makeText(this, "Schedule conflict detected", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Server error: " + response, Toast.LENGTH_LONG).show();
                        Log.e("AddScheduleActivity", "Response: " + response);
                    }
                },
                error -> {
                    Toast.makeText(this, "Error submitting schedule", Toast.LENGTH_SHORT).show();
                    Log.e("AddScheduleActivity", "Error: " + error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("teacher_id", String.valueOf(teacher.id));
                params.put("subject_id", String.valueOf(subject.id));
                params.put("class_id", String.valueOf(schoolClass.id));
                params.put("day_of_week", day);
                params.put("start_time", start);
                params.put("end_time", end);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }


    class Teacher {
        int id;
        String name;

        Teacher(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    class Subject {
        int id;
        String name;

        Subject(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    class SchoolClass {
        int id;
        String name;

        SchoolClass(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
