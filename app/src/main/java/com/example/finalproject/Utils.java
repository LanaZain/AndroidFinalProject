package com.example.finalproject;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
