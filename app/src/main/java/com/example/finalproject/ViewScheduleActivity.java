package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private ArrayList<ScheduleItem> list;
//    private static final String URL =  getString(R.string.ip)+ "shaimaa/get_schedule.php?class_id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        recyclerView = findViewById(R.id.recyclerViewSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new ScheduleAdapter(list);
        recyclerView.setAdapter(adapter);

        int classId = getIntent().getIntExtra("class_id", -1);
        if (classId != -1) {
            fetchSchedule(classId);
        } else {
            Toast.makeText(this, "معرف الصف غير موجود", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchSchedule(int classId) {
        String url =  getString(R.string.ip)+ "shaimaa/get_schedule.php?class_id=" + classId;
        RequestQueue q = Volley.newRequestQueue(this);

        StringRequest r = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getBoolean("success")) {
                            list.clear();
                            JSONArray arr = json.getJSONArray("schedule");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject o = arr.getJSONObject(i);
                                list.add(new ScheduleItem(
                                        o.getString("subject_name"),
                                        o.getString("day_of_week"),
                                        o.getString("start_time"),
                                        o.getString("end_time"),
                                        o.getString("teacher_name")  // تم التعديل هنا
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, json.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "خطأ في تحليل البيانات", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "خطأ في الاتصال بالخادم", Toast.LENGTH_SHORT).show()
        );

        q.add(r);
    }

    // Model class
    public static class ScheduleItem {
        private String subjectName, dayOfWeek, startTime, endTime, teacherName;

        public ScheduleItem(String subjectName, String dayOfWeek, String startTime, String endTime, String teacherName) {
            this.subjectName = subjectName;
            this.dayOfWeek = dayOfWeek;
            this.startTime = startTime;
            this.endTime = endTime;
            this.teacherName = teacherName;
        }

        public String getSubjectName() { return subjectName; }
        public String getDayOfWeek() { return dayOfWeek; }
        public String getStartTime() { return startTime; }
        public String getEndTime() { return endTime; }
        public String getTeacherName() { return teacherName; }
    }

    // Adapter
    public static class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.VH> {
        private final List<ScheduleItem> list;

        public ScheduleAdapter(List<ScheduleItem> list) {
            this.list = list;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_schedule, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            ScheduleItem item = list.get(position);
            holder.subject.setText(item.getSubjectName());
            holder.day.setText("اليوم: " + item.getDayOfWeek());
            holder.time.setText("الوقت: " + item.getStartTime() + " - " + item.getEndTime());
            holder.teacher.setText("المدرس: " + item.getTeacherName()); // تم التعديل هنا
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            TextView subject, day, time, teacher;

            VH(View v) {
                super(v);
                subject = v.findViewById(R.id.subjectName);
                day = v.findViewById(R.id.day);
                time = v.findViewById(R.id.time);
                teacher = v.findViewById(R.id.teacher);
            }
        }
    }
}
