package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubmitHomeworkActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 101;
    private ChipGroup fileChipGroup;
    private FloatingActionButton submitFab;
    private ProgressBar progressBar;

    private ArrayList<Uri> selectedFiles;
    private int assignmentId;
    private int studentId = 1; // HARDCODED: Replace with actual logged-in student ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_homework);

        fileChipGroup = findViewById(R.id.fileChipGroup);
        Button pickFilesButton = findViewById(R.id.pickFilesButton);
        submitFab = findViewById(R.id.submitFab);
        progressBar = findViewById(R.id.progressBar); // Make sure you have a ProgressBar in your XML layout

        selectedFiles = new ArrayList<>();

        // Get assignment ID from the previous activity
        assignmentId = getIntent().getIntExtra("assignment_id", -1);
        if (assignmentId == -1) {
            Toast.makeText(this, "Error: Assignment ID not found.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        pickFilesButton.setOnClickListener(v -> openFilePicker());

        submitFab.setOnClickListener(v -> {
            if (selectedFiles.isEmpty()) {
                Toast.makeText(this, "Please select a file to submit.", Toast.LENGTH_SHORT).show();
            } else {
                // For simplicity, this example uploads only the first selected file.
                uploadSubmission(selectedFiles.get(0));
            }
        });
    }

    // In SubmitHomeworkActivity.java

    // In SubmitHomeworkActivity.java
    private void uploadSubmission(Uri fileUri) {
        setLoading(true);

        String baseUrl = getString(R.string.ip); // or getApplicationContext().getString(...) if needed
        String url = baseUrl + "/mobileProject/assignments.php?action=submit_assignment";


        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                response -> {
                    // ... (your existing success code is fine) ...
                    setLoading(false);
                    String resultResponse = new String(response.data);
                    try {
                        JSONObject result = new JSONObject(resultResponse);
                        Toast.makeText(SubmitHomeworkActivity.this, result.getString("message"), Toast.LENGTH_LONG).show();
                        if (result.getBoolean("success")) {
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // ... (your existing error code is fine) ...
                    setLoading(false);
                    String errorMessage = "Unknown error";
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null) {
                        String responseData = new String(networkResponse.data);
                        errorMessage = "Error " + networkResponse.statusCode + ": " + responseData;
                    } else if (error.getMessage() != null){
                        errorMessage = error.getMessage();
                    }
                    Log.e("VOLLEY_ERROR", "Upload failed: " + errorMessage);
                    Toast.makeText(SubmitHomeworkActivity.this, "Upload failed: " + errorMessage, Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
             //   params.put("action", "submit_assignment");
                params.put("assignment_id", String.valueOf(assignmentId));
                params.put("student_id", String.valueOf(studentId));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    // =======================================================
                    //  ADDED LOGGING TO CHECK FILE READING
                    // =======================================================
                    Log.d("FILE_READ", "Attempting to read file: " + getFileName(fileUri));
                    byte[] fileData = getBytesFromUri(fileUri);
                    Log.d("FILE_READ", "Successfully read " + fileData.length + " bytes.");
                    // =======================================================

                    params.put("file", new DataPart(getFileName(fileUri), fileData));
                } catch (IOException e) {
                    // =======================================================
                    //  THIS IS A LIKELY SOURCE OF THE "UNKNOWN ERROR"
                    // =======================================================
                    Log.e("FILE_READ_ERROR", "Failed to read file into bytes.", e);
                    // =======================================================
                    e.printStackTrace();
                }
                return params;
            }
        };

        // =======================================================
        //  STEP 1: INCREASE THE TIMEOUT FOR THE REQUEST
        // =======================================================
        int timeoutMs = 30000; // 30 seconds
        multipartRequest.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                timeoutMs,
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // =======================================================

        Volley.newRequestQueue(this).add(multipartRequest);
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            submitFab.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            submitFab.setEnabled(true);
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        // To allow multiple file selection, though we only upload one in this example
        // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            fileChipGroup.removeAllViews();
            selectedFiles.clear();

            // This handles both single and multiple file selection, but we'll focus on one
            if (data.getClipData() != null) {
                Uri fileUri = data.getClipData().getItemAt(0).getUri();
                addFileChip(fileUri);
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
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public byte[] getBytesFromUri(Uri uri) throws IOException {
        InputStream iStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = iStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}