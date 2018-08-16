package com.haley.base;

import android.util.Log;

/**
 * Created by haley on 2018/7/26.
 */

public class LogUtil {
    public static final String TAG = "HyStruct";

    public static void i(String message) {
        Log.i(TAG, message);
    }

    public static void w(String message) {
        Log.w(TAG, message);
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }
}
