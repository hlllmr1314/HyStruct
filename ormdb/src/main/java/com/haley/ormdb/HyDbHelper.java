package com.haley.ormdb;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * </p>
 * Author: haley
 * Email : leihuang@ecarx.com.cn
 * Date  : 2018/8/16.
 * Desc  : HyDbHelper 不对外提供
 * </p>
 */

public final class HyDbHelper extends SQLiteOpenHelper {

    private HyDbManager dbManager;

    protected HyDbHelper(String dbName, int dbVersion) {
        super(HyDatabase.getContext(), dbName, null, dbVersion);
        dbManager = HyDbManager.getInstance();
        getReadableDatabase().close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        dbManager.createDb(db, getDatabaseName());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            dbManager.updateDb(db, getDatabaseName());
        }
    }
}
