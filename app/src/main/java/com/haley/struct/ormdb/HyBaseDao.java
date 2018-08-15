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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.haley.struct.ormdb.HyDbUtil.checkIsDefalutValue;
import static com.haley.struct.ormdb.HyDbUtil.getCreateTableSql;
import static com.haley.struct.ormdb.HyDbUtil.getPrimaryKeyColumnName;
import static com.haley.struct.ormdb.HyDbUtil.getQueryByIdsSql;
import static com.haley.struct.ormdb.HyDbUtil.getQueryEmptyTableSql;
import static com.haley.struct.ormdb.HyDbUtil.getQueryAllSql;
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
        sqLiteDatabase.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

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

                    //针对自增的属性，如果传入参数为0，则进行忽视
                    if (field.getAnnotation(HyField.class).isAutoIncrement() && checkIsDefalutValue(value)) {
                        continue;
                    }

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

        LogUtil.w("getContentValues:" + contentValues.toString());

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
        Cursor cursor = sqLiteDatabase.rawQuery(getQueryAllSql(tbClass), new String[]{});
        return ergodicList(cursor);
    }

    @Override
    public List<T> findAll(Iterable<ID> var1) {

        if (var1 == null || !var1.iterator().hasNext()) {
            return null;
        }

        StringBuffer stringBuffer = new StringBuffer();
        Iterator iterator = var1.iterator();
        while (iterator.hasNext()) {
            if (stringBuffer.length() != 0) {
                stringBuffer.append(",");
            }
            stringBuffer.append(String.valueOf(iterator.next()));
        }

        String[] selects = stringBuffer.toString().split(",");

        LogUtil.w("selectArgs:" + selects);

        Cursor cursor = sqLiteDatabase.rawQuery(getQueryByIdsSql(tbClass, var1)
                , selects);
        return ergodicList(cursor);
    }

    private List ergodicList(Cursor cursor) {
        if (cursor != null) {
            try {
                List<T> lists = new ArrayList<>();
                T entity;

                String key;
                Field field;
                Map.Entry<String, Field> entry;
                while (cursor.moveToNext()) {

                    entity = tbClass.newInstance();

                    Iterator<Map.Entry<String, Field>> iterator = arrayMap.entrySet().iterator();

                    while (iterator.hasNext()) {
                        entry = iterator.next();
                        key = entry.getKey();
                        field = entry.getValue();

                        field.setAccessible(true);

                        int columnIndex = cursor.getColumnIndex(key);

                        if (field.getType() == String.class) {
                            field.set(entity, cursor.getString(columnIndex));
                        } else if (field.getType() == Integer.class) {
                            field.set(entity, cursor.getInt(columnIndex));
                        } else if (field.getType() == int.class) {
                            field.setInt(entity, cursor.getInt(columnIndex));
                        } else if (field.getType() == Double.class) {
                            field.set(entity, cursor.getDouble(columnIndex));
                        } else if (field.getType() == double.class) {
                            field.setDouble(entity, cursor.getDouble(columnIndex));
                        } else if (field.getType() == Long.class) {
                            field.set(entity, cursor.getLong(columnIndex));
                        } else if (field.getType() == long.class) {
                            field.setLong(entity, cursor.getLong(columnIndex));
                        } else if (field.getType() == Float.class) {
                            field.set(entity, cursor.getFloat(columnIndex));
                        } else if (field.getType() == float.class) {
                            field.setFloat(entity, cursor.getFloat(columnIndex));
                        } else if (field.getType() == byte[].class) {
                            field.set(entity, cursor.getBlob(columnIndex));
                        }
                    }

                    lists.add(entity);
                }

                return lists;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }

        return null;
    }

    @Override
    public void deleteAll() {
        sqLiteDatabase.delete(tableName, null, new String[]{});
    }

    @Override
    public void delete(ID var1) {
        String primaryKey = getPrimaryKeyColumnName(tbClass);
        sqLiteDatabase.delete(tableName, primaryKey + "=?"
                , new String[]{String.valueOf(var1)});
    }

    @Override
    public boolean delete(Iterable<? extends ID> var1) {

        if (var1 == null || !var1.iterator().hasNext()) {
            return false;
        }

        String primaryKey = getPrimaryKeyColumnName(tbClass);

        StringBuffer selectSb = new StringBuffer();
        StringBuffer whereSb = new StringBuffer();

        whereSb.append(primaryKey + " IN(");

        Iterator iterator = var1.iterator();
        while (iterator.hasNext()) {
            if (selectSb.length() != 0) {
                selectSb.append(",");
                whereSb.append(",");
            }
            whereSb.append("?");
            selectSb.append(String.valueOf(iterator.next()));
        }
        whereSb.append(")");

        String[] selects = selectSb.toString().split(",");
        String whereStr = whereSb.toString();

        LogUtil.w("selectArgs:" + selects);
        LogUtil.w("whereStr:" + whereStr);
        sqLiteDatabase.delete(tableName, whereStr, selects);

        return true;
    }

    @Override
    public void delete(T var1) {

    }

    @Override
    public void execSQL(String sql) {
        execSQL(sql, new String[]{});
    }

    @Override
    public void execSQL(String sql, Object[] bindArgs) {
        LogUtil.w("execSQL:" + sql + " args:" + bindArgs);
        sqLiteDatabase.execSQL(sql, bindArgs);
    }
}
