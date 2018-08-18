package com.haley.base;

/**
 * Created by haley on 2018/7/26.
 */

public final class LogUtil {

    public static final String TAG = "HyStruct";

    private static LogUtil mInstance = new LogUtil();
    protected ILog log = new HyLogcat();
    protected String globalTag;

    private LogUtil() {

    }

    public static LogUtil builder() {
        if (mInstance == null) {
            mInstance = new LogUtil();
        }
        return mInstance;
    }

    public LogUtil setLog(ILog log) {
        this.log = log;
        return this;
    }

    public LogUtil setGlobalTag(String globalTag) {
        this.globalTag = globalTag;
        return this;
    }

    public static void i(String message) {
        mInstance.log.i(mInstance.globalTag, message);
    }

    public static void i(String tag, String message) {
        mInstance.log.i(tag, message);
    }

    public static void d(String message) {
        mInstance.log.d(mInstance.globalTag, message);
    }

    public static void d(String tag, String message) {
        mInstance.log.d(tag, message);
    }

    public static void w(String message) {
        mInstance.log.w(mInstance.globalTag, message);
    }


    public static void w(String tag, String message) {
        mInstance.log.w(tag, message);
    }

    public static void e(String message) {
        mInstance.log.e(mInstance.globalTag, message);
    }

    public static void e(String tag, String message) {
        mInstance.log.e(tag, message);
    }
}
