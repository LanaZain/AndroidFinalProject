package com.example.finalproject;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentsListActivity extends AppCompatActivity {

    private ListView studentListView;
    private final ArrayList<String> studentList = new ArrayList<>();
//    private static final String BASE_URL = getString(R.string.ip)+ "shaimaa/get_students_by_class.php?class_id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        studentListView = findViewById(R.id.studentListView);

        int classId = getIntent().getIntExtra("class_id", -1);

        // تأكد من استقبال class_id
        Toast.makeText(this, "Received class_id: " + classId, Toast.LENGTH_SHORT).show();

        if (classId != -1) {
            loadStudentsByClass(classId);
        } else {
            Toast.makeText(this, "لم يتم تمرير class_id", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadStudentsByClass(int classId) {
        String url = getString(R.string.ip)+ "shaimaa/get_students_by_class.php?class_id=" + classId;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        studentList.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String fullName = obj.getString("full_name");
                            String studentNumber = obj.getString("student_number");
                            String major = obj.getString("major");
                            studentList.add(fullName + "\n" + studentNumber + "\n" + major);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_list_item_2, android.R.id.text1, studentList) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);

                                TextView text1 = view.findViewById(android.R.id.text1);
                                TextView text2 = view.findViewById(android.R.id.text2);

                                String item = getItem(position);
                                if (item != null) {
                                    String[] parts = item.split("\n");
                                    if (parts.length >= 3) {
                                        text1.setText(parts[0]);
                                        text2.setText("Student Number: " + parts[1] + "\nMajor: " + parts[2]);
                                        text1.setTextSize(20);
                                        text2.setTextSize(16);
                                        text1.setTextColor(Color.parseColor("#C19494"));
                                        text2.setTextColor(Color.parseColor("#C19494"));
                                    } else {
                                        text1.setText(item);
                                        text2.setText("");
                                    }
                                }

                                view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                                return view;
                            }
                        };

                        studentListView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "خطأ في قراءة البيانات", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "فشل تحميل الطلاب", Toast.LENGTH_SHORT).show();
                    Log.e("StudentsListActivity", "Fetch error: " + error.toString());
                });

        Volley.newRequestQueue(this).add(request);
    }
}