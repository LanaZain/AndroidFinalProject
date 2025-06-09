package com.example.finalproject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        prepareScheduleData();
        setupDayButtons();
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
                if (selectedButton != null) {
                    selectedButton.setSelected(false);
                }
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

    private void prepareScheduleData() {
        scheduleMap = new HashMap<>();
        scheduleMap.put("Sunday", List.of(
                new ScheduleItem("8:00 AM", "Mathematics", "Room 101"),
                new ScheduleItem("9:15 AM", "Arabic", "Room 102"),
                new ScheduleItem("10:30 AM", "Religion", "Room 103"),
                new ScheduleItem("11:45 AM", "English", "Room 104"),
                new ScheduleItem("1:00 PM", "Mathematics", "Room 101"),
                new ScheduleItem("2:15 PM", "Physics", "Room 105"),
                new ScheduleItem("3:30 PM", "Biology", "Room 106"),
                new ScheduleItem("4:45 PM", "Arts", "Room 107")
        ));
        scheduleMap.put("Monday", List.of(
                new ScheduleItem("8:00 AM", "English", "Room 104"),
                new ScheduleItem("9:15 AM", "History", "Room 108"),
                new ScheduleItem("10:30 AM", "Chemistry", "Room 109"),
                new ScheduleItem("11:45 AM", "Mathematics", "Room 101"),
                new ScheduleItem("1:00 PM", "Arabic", "Room 102"),
                new ScheduleItem("2:15 PM", "Physics", "Room 105")
        ));
        // Add rest of the days similarly...
    }
}
