package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {
    private static final String PREFS_NAME = "SchoolSystemPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_TYPE = "userType";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_CLASS_ID = "classId";
    private static final String KEY_TEACHER_ID = "teacherId"; // ✅ جديد

    private final SharedPreferences sharedPreferences;

    public AuthManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // ✅ الحفظ يشمل class_id و teacher_id حسب النوع
    public void saveLoginData(String email, String userType, String name, String userId, String classId, String teacherId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USER_TYPE, userType);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_CLASS_ID, classId);       // للطالب
        editor.putString(KEY_TEACHER_ID, teacherId);   // للمعلم
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserType() {
        return sharedPreferences.getString(KEY_USER_TYPE, "");
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getName() {
        return sharedPreferences.getString(KEY_NAME, "");
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, "");
    }

    public String getClassId() {
        return sharedPreferences.getString(KEY_CLASS_ID, "");
    }

    public String getTeacherId() {
        return sharedPreferences.getString(KEY_TEACHER_ID, "");
    }

    public void logout() {
        sharedPreferences.edit().clear().apply();
    }
}
