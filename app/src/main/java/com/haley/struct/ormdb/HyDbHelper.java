package com.haley.struct.ormdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.haley.struct.HyApp;

/**
 * Created by haley on 2018/7/26.
 */

public class HyDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "demo.db";
    private static final int DB_VERSION = 1;

    private static HyDbHelper dbHelper;

    public static HyDbHelper getInstance() {
        if (dbHelper == null) {
            synchronized (HyDbHelper.class) {
                if (dbHelper == null) {
                    dbHelper = new HyDbHelper();
                }
            }
        }

        return dbHelper;
    }

    private HyDbHelper() {
        this(HyApp.getContext());
    }

    private HyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
