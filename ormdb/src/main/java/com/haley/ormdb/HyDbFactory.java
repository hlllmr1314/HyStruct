package com.haley.ormdb;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.ArrayMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * </p>
 * Author: haley
 * Email : leihuang@ecarx.com.cn
 * Date  : 2018/8/16.
 * Desc  : 框架工厂类，对外数据库操作具柄
 * </p>
 */

public final class HyDbFactory {

    private static HyDbFactory mInstance;
    private Map<String, HyBaseDao> daoCahces;

    public static HyDbFactory getInstance() {
        if (mInstance == null) {
            synchronized (HyDbFactory.class) {
                if (mInstance == null) {
                    mInstance = new HyDbFactory();
                }
            }
        }
        return mInstance;
    }

    private HyDbFactory() {
        this.daoCahces = new ArrayMap<>();
    }

    public synchronized <T, ID extends Serializable> HyBaseDao<T, ID> getBaseDao(Class<T> tbClass) {
        return getBaseDao(HyDatabase.getInstance().getSingleDbHelper(), tbClass);
    }

    public synchronized <T, ID extends Serializable> HyBaseDao<T, ID> getBaseDao(String dbName, Class<T> tbClass) {
        return getBaseDao(HyDatabase.getInstance().getDbHelperByDbName(dbName), tbClass);
    }

    private synchronized <T, ID extends Serializable> HyBaseDao<T, ID> getBaseDao(@NonNull HyDbHelper dbHelper, Class<T> tbClass) {

        if (dbHelper == null) {
            return null;
        }

        HyBaseDao baseDao = daoCahces.get(dbHelper.getDatabaseName() + tbClass.getSimpleName());

        if (baseDao != null) {
            return baseDao;
        }

        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            baseDao = HyBaseDao.class.newInstance();
            baseDao.init(sqLiteDatabase, tbClass);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        daoCahces.put(dbHelper.getDatabaseName() + tbClass.getSimpleName(), baseDao);

        return baseDao;
    }

}
