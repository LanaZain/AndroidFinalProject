package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GradesActivity extends AppCompatActivity {

    private TableLayout gradesTable;
    private final int currentStudentId = 1; // You can set this dynamically as needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        gradesTable = findViewById(R.id.gradesTable);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        fetchGrades();
    }

    private void fetchGrades() {
        String baseUrl = getApplicationContext().getString(R.string.ip);
        String url = baseUrl + "/mobileProject/grades.php?action=list_grades_for_student&student_id=" + currentStudentId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {

                            JSONArray assignments = response.optJSONArray("assignments");
                            JSONArray exams = response.optJSONArray("exams");

                            if (assignments != null) {
                                for (int i = 0; i < assignments.length(); i++) {
                                    JSONObject obj = assignments.getJSONObject(i);
                                    String subject = obj.getString("subject");
                                    String title = obj.getString("title");
                                    String points = obj.getString("points_earned");
                                    addGradeRow(subject, title, points);
                                }
                            }

                            if (exams != null) {
                                for (int i = 0; i < exams.length(); i++) {
                                    JSONObject obj = exams.getJSONObject(i);
                                    String subject = obj.getString("subject");
                                    String title = obj.getString("title");
                                    String score = obj.getString("score");
                                    addGradeRow(subject, title, score);
                                }
                            }

                            if ((assignments == null || assignments.length() == 0) &&
                                    (exams == null || exams.length() == 0)) {
                                Toast.makeText(this, "No grades found.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(this, response.optString("message", "Failed to load grades"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Error parsing grades JSON: " + e.getMessage());
                        Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VOLLEY_ERROR", "Error fetching grades: " + error.toString());
                    Toast.makeText(this, "Failed to fetch grades", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void addGradeRow(String subject, String assignment, String grade) {
        TableRow row = new TableRow(this);

        TextView subjectView = createCell(subject, false);
        TextView assignmentView = createCell(assignment, false);
        TextView gradeView = createCell(grade, true);

        row.addView(subjectView);
        row.addView(assignmentView);
        row.addView(gradeView);

        gradesTable.addView(row);

        // Divider
        View divider = new View(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                2
        );
        params.span = 3;
        divider.setLayoutParams(params);
        divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        TableRow dividerRow = new TableRow(this);
        dividerRow.addView(divider);
        gradesTable.addView(dividerRow);
    }

    private TextView createCell(String text, boolean isBold) {
        TextView cell = new TextView(this);
        if (isBold) {
            cell.setTextAppearance(this, R.style.GradeTableCellBold);
        } else {
            cell.setTextAppearance(this, R.style.GradeTableCell);
        }
        cell.setText(text);
        return cell;
    }
}
