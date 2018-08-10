package com.haley.struct.ormdb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;
import android.util.Log;

import com.haley.struct.LogUtil;
import com.haley.struct.ormdb.annotation.HyField;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.haley.struct.ormdb.HyDbUtil.getCreateTableSql;
import static com.haley.struct.ormdb.HyDbUtil.getQueryEmptyTableSql;
import static com.haley.struct.ormdb.HyDbUtil.getTableName;

/**
 * Created by haley on 2018/7/26.
 */

public class HyBaseDao<T, ID extends Serializable> implements HyDao<T, ID> {

    protected Class<T> tbClass;
    protected SQLiteDatabase sqLiteDatabase;

    private String tableName;
    private ArrayMap<String, Field> arrayMap;

    public HyBaseDao() {

    }

    private boolean isInit = false;

    protected synchronized boolean init(SQLiteDatabase sqLiteDatabase, Class<T> tClass) {
        if (!isInit) {
            this.tbClass = tClass;
            this.tableName = getTableName(tClass);
            this.sqLiteDatabase = sqLiteDatabase;

            if (!sqLiteDatabase.isOpen()) {
                return false;
            }

            //1,建表
            if (!autoCreateTable()) {
                return false;
            }

            isInit = true;
        }

        //2.缓存一下表的字段名和成员变量的映射关系，这样方便查询和防止表和字段不一致的问题
        initCacheMap();
        return true;
    }

    @Override
    public void close() {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
        isInit = false;
    }

    /**
     * 自动建表
     *
     * @return
     */
    private boolean autoCreateTable() {
        try {
            sqLiteDatabase.execSQL(getCreateTableSql(tbClass));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void initCacheMap() {
        if (arrayMap == null) {
            arrayMap = new ArrayMap();
        } else {
            arrayMap.clear();
        }

        Cursor cursor = sqLiteDatabase.rawQuery(getQueryEmptyTableSql(tbClass), new String[]{});
        if (cursor != null) {
            String[] columnNames = cursor.getColumnNames();
            if (columnNames != null) {
                for (String columnName : columnNames) {
                    for (Field field : tbClass.getDeclaredFields()) {
                        if (columnName.equals(field.getAnnotation(HyField.class).value())) {
                            arrayMap.put(columnName, field);
                            break;
                        }
                    }
                }
            }
            cursor.close();
        }

        LogUtil.w("initCacheMap :" + arrayMap);
    }

    @Override
    public Long save(T var1) {

        if (sqLiteDatabase == null) {
            LogUtil.w("sqLiteDatabase is null!");
            return -1L;
        }

        if (!sqLiteDatabase.isOpen()) {
            LogUtil.w("sqLiteDatabase is open! plase execute init again!");
            return -1L;
        }

        ContentValues contentValues = getContentValues(var1);
        sqLiteDatabase.insert(tableName, null, contentValues);

        return -1L;
    }

    private ContentValues getContentValues(T entity) {
        ContentValues contentValues = new ContentValues();
        try {
            Iterator<Map.Entry<String, Field>> iterable = arrayMap.entrySet().iterator();
            String key;
            Field field;
            Object value;
            while (iterable.hasNext()) {
                key = iterable.next().getKey();
                field = arrayMap.get(key);

                field.setAccessible(true);

                value = field.get(entity);
                if (value != null) {
                    if (value instanceof String) {
                        contentValues.put(key, (String) value);
                    } else if (value instanceof Integer) {
                        contentValues.put(key, (Integer) value);
                    } else if (value instanceof Double) {
                        contentValues.put(key, (Double) value);
                    } else if (value instanceof Long) {
                        contentValues.put(key, (Long) value);
                    } else if (value instanceof byte[]) {
                        contentValues.put(key, (byte[]) value);
                    } else if (value instanceof Float) {
                        contentValues.put(key, (Float) value);
                    } else {
                        LogUtil.w(value.getClass() + "类型不支持");
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        LogUtil.w(contentValues.toString());

        return contentValues;
    }

    @Override
    public boolean save(Iterable<T> var1) {
        sqLiteDatabase.beginTransaction();
        try {
            Iterator<T> iterator = var1.iterator();
            while (iterator.hasNext()) {
                if (save(iterator.next()) > -1L) {
                    return false;
                }
            }
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return true;
    }

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public List<T> findAll(Iterable var1) {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void delete(Serializable var1) {

    }

    @Override
    public void delete(Iterable var1) {

    }

    @Override
    public void delete(Object var1) {

    }
}
