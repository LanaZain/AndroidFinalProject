package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;

public class GradesActivity extends AppCompatActivity {

    private TableLayout gradesTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        gradesTable = findViewById(R.id.gradesTable);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Sample data
        String[][] gradeData = {
                {"Mathematics", "Ass1-Quadratic Equations", "85"},
                {"History", "Ass1-World War2", "75"},
                {"English", "Ass1-Simple Past", "98"},
                {"Biology", "Quiz1", "92"},
        };

        for (String[] row : gradeData) {
            addGradeRow(row[0], row[1], row[2]);
        }
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

        // Add a divider
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
