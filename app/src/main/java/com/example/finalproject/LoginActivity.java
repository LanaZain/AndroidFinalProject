package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Spinner userTypeSpinner;
    private Button loginButton;
    private ProgressBar progressBar;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ✅ تفعيل الوضع الليلي
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDarkMode = preferences.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toast.makeText(this, "تم فتح LoginActivity", Toast.LENGTH_SHORT).show();

        // ربط عناصر الواجهة
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        userTypeSpinner = findViewById(R.id.userTypeSpinner);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        TextView registerLink = findViewById(R.id.registerLink);

        authManager = new AuthManager(this);

        // تعبئة السبينر بأنواع المستخدمين
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);

        // إذا المستخدم مسجل دخول مسبقًا
        if (authManager.isLoggedIn()) {
            navigateToHome();
            return;
        }

        // الضغط على تسجيل الدخول
        loginButton.setOnClickListener(v -> attemptLogin());

        // الذهاب للتسجيل
        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String selected = userTypeSpinner.getSelectedItem().toString();
        String userType;

        // تحويل القيم من العربي إلى النوع الفعلي
        switch (selected) {
            case "طالب":
                userType = "student";
                break;
            case "معلم":
                userType = "teacher";
                break;
            case "مسجل":
                userType = "registrar";
                break;
            default:
                userType = "";
        }

        // تحقق من الحقول
        if (email.isEmpty()) {
            emailEditText.setError("البريد الإلكتروني مطلوب");
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("كلمة المرور مطلوبة");
            return;
        }

        if (userType.isEmpty()) {
            Toast.makeText(this, "يرجى اختيار نوع المستخدم", Toast.LENGTH_SHORT).show();
            return;
        }

        // إظهار التحميل
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        // إعداد بيانات الطلب
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("user_type", userType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // رابط API
        String url =  getString(R.string.ip)+ "shaimaa/login.php";

        // إرسال الطلب
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    loginButton.setEnabled(true);
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            JSONObject user = response.getJSONObject("user");

                            authManager.saveLoginData(
                                    user.getString("email"),
                                    user.getString("user_type"),
                                    user.getString("name"),
                                    user.getString("id"),
                                    user.optString("class_id", ""),
                                    user.optString("teacher_id", "")
                            );

                            navigateToHome();
                        } else {
                            String message = response.getString("message");
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "حدث خطأ أثناء تحليل الرد", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    loginButton.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "فشل الاتصال بالخادم: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );

        // تنفيذ الطلب
        Volley.newRequestQueue(this).add(request);
    }

    private void navigateToHome() {
        String userType = authManager.getUserType();
        Intent intent;

        switch (userType) {
            case "student":
                intent = new Intent(this, MainActivity.class);
                break;
            case "teacher":
                intent = new Intent(this, AddSubjectActivity.class);
                break;
            case "registrar":
                intent = new Intent(this, RegistrarActivity.class);
                break;
            default:
                Toast.makeText(this, "نوع المستخدم غير معروف", Toast.LENGTH_SHORT).show();
                return;
        }

        startActivity(intent);
        finish();
    }
}
