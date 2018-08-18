package com.haley.base;

import android.util.Log;

/**
 * </p>
 * Author: haley
 * Email : leihuang@ecarx.com.cn
 * Date  : 2018/8/18
 * Desc  : HyStruct
 * </p>
 */
public final class HyLogcat implements ILog {

    public static HyLogcat log() {
        return new HyLogcat();
    }

    @Override
    public void i(String tag, String message) {
        Log.i(tag, message);
    }

    @Override
    public void d(String tag, String message) {
        Log.d(tag, message);
    }

    @Override
    public void w(String tag, String message) {
        Log.w(tag, message);
    }

    @Override
    public void e(String tag, String message) {
        Log.e(tag, message);
    }
}
