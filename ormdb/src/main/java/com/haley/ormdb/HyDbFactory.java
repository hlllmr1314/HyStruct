package com.haley.ormdb;

import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.HashMap;
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

    }

    public synchronized <T, ID extends Serializable> HyBaseDao<T, ID> getBaseDao(Class<T> tbClass) {
        HyBaseDao baseDao = null;

        try {
            HyDbHelper dbHelper = HyDatabase.getInstance().getSingleDbHelper();
            if (dbHelper == null) {
                return null;
            }

            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
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
