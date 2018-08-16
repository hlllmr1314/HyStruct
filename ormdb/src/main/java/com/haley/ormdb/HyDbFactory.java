package com.haley.ormdb;

import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

/**
 * Created by haley on 2018/7/26.
 */

public final class HyDbFactory {

    private static HyDbFactory mInstance;

    private HyDbHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

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
        //数据库初始化操作
        this.dbHelper = HyDbHelper.getInstance();
        this.sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public synchronized <T, ID extends Serializable> HyBaseDao<T, ID> getBaseDao(Class<T> tbClass) {
        HyBaseDao baseDao = null;

        try {
            baseDao = HyBaseDao.class.newInstance();
            baseDao.init(sqLiteDatabase, tbClass);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return baseDao;
    }

}
