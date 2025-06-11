package com.example.finalproject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};
    Button selectedButton;
    LinearLayout dayButtonsLayout;
    RecyclerView scheduleRecyclerView;
    ScheduleAdapter adapter;
    HashMap<String, List<ScheduleItem>> scheduleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        TextView headerTitle = findViewById(R.id.headerTitle);
        headerTitle.setText("Schedule");

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        dayButtonsLayout = findViewById(R.id.dayButtonsLayout);
        scheduleRecyclerView = findViewById(R.id.scheduleRecyclerView);
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ScheduleAdapter(new ArrayList<>());
        scheduleRecyclerView.setAdapter(adapter);

        initializeEmptySchedule();
        setupDayButtons();
        fetchScheduleData();
    }

    private void initializeEmptySchedule() {
        scheduleMap = new HashMap<>();
        for (String day : days) {
            scheduleMap.put(day, getFreeSchedule());
        }
    }

    private List<ScheduleItem> getFreeSchedule() {
        List<ScheduleItem> freeList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            freeList.add(new ScheduleItem(getClassTime(i), "Free", ""));
        }
        return freeList;
    }

    private void setupDayButtons() {
        for (String day : days) {
            Button button = new Button(this);
            button.setText(day);
            button.setAllCaps(false);
            button.setTextColor(ContextCompat.getColor(this, R.color.red_800));
            button.setTypeface(null, Typeface.BOLD);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.day_button_selector));
            button.setPadding(16, 32, 16, 32);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 12, 0, 0);
            button.setLayoutParams(params);

            button.setOnClickListener(v -> {
                if (selectedButton != null) selectedButton.setSelected(false);
                button.setSelected(true);
                selectedButton = button;
                adapter.updateData(scheduleMap.get(day));
            });

            if (day.equals("Sunday")) {
                button.setSelected(true);
                selectedButton = button;
                adapter.updateData(scheduleMap.get(day));
            }

            dayButtonsLayout.addView(button);
        }
    }

    private void fetchScheduleData() {
        String url = getString(R.string.ip) + "/mobileProject/schedule.php?action=list_schedule";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (String day : days) {
                            JSONArray dayArray = response.optJSONArray(day);
                            List<ScheduleItem> daySchedule = new ArrayList<>();

                            if (dayArray != null && dayArray.length() > 0) {
                                JSONObject classData = dayArray.getJSONObject(0);

                                for (int i = 1; i <= 8; i++) {
                                    String periodKey = getClassKey(i) + "_name";  // e.g., "first_class"
                                    String className = classData.optString(periodKey, "Free");
                                    String teacherName = classData.optString("teacher_name", "");

                                    // Fallback if null or blank
                                    if (className == null || className.equals("null")) className = "Free";
                                    if (teacherName == null || teacherName.equals("null")) teacherName = "";

                                    daySchedule.add(new ScheduleItem(getClassTime(i), className, teacherName));
                                }
                            } else {
                                daySchedule = getFreeSchedule(); // Empty means all Free
                            }

                            scheduleMap.put(day, daySchedule);
                        }

                        if (selectedButton != null) {
                            String selectedDay = selectedButton.getText().toString();
                            adapter.updateData(scheduleMap.get(selectedDay));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private String getClassKey(int index) {
        switch (index) {
            case 1: return "first_class";
            case 2: return "second_class";
            case 3: return "third_class";
            case 4: return "fourth_class";
            case 5: return "fifth_class";
            case 6: return "sixth_class";
            case 7: return "seventh_class";
            case 8: return "eighth_class";
            default: return "";
        }
    }

    private String getClassTime(int index) {
        switch (index) {
            case 1: return "8:00 AM";
            case 2: return "9:15 AM";
            case 3: return "10:30 AM";
            case 4: return "11:45 AM";
            case 5: return "1:00 PM";
            case 6: return "2:15 PM";
            case 7: return "3:30 PM";
            case 8: return "4:45 PM";
            default: return "";
        }
    }
}
