package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SubmitHomeworkActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 101;
    private ChipGroup fileChipGroup;
    private ArrayList<Uri> selectedFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_homework);

        fileChipGroup = findViewById(R.id.fileChipGroup);
        Button pickFilesButton = findViewById(R.id.pickFilesButton);
        FloatingActionButton submitFab = findViewById(R.id.submitFab);

        selectedFiles = new ArrayList<>();

        pickFilesButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Select Files"), PICK_FILE_REQUEST_CODE);
        });

        submitFab.setOnClickListener(v -> {
            // TODO: Handle file upload or submission logic
            if (selectedFiles.isEmpty()) {
                Utils.showToast(this, "Please select at least one file.");
            } else {
                Utils.showToast(this, "Submitting " + selectedFiles.size() + " files.");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            fileChipGroup.removeAllViews();
            selectedFiles.clear();

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    addFileChip(fileUri);
                }
            } else if (data.getData() != null) {
                Uri fileUri = data.getData();
                addFileChip(fileUri);
            }
        }
    }

    private void addFileChip(Uri fileUri) {
        String fileName = getFileName(fileUri);
        Chip chip = new Chip(this);
        chip.setText(fileName);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            fileChipGroup.removeView(chip);
            selectedFiles.remove(fileUri);
        });
        fileChipGroup.addView(chip);
        selectedFiles.add(fileUri);
    }

    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String name = cursor.getString(nameIndex);
        cursor.close();
        return name;
    }
}
