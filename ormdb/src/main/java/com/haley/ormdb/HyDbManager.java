package com.haley.ormdb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.haley.base.LogUtil;
import com.haley.ormdb.annotation.HyField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.haley.ormdb.HyDbUtil.getAlertTableColumnNameSql;
import static com.haley.ormdb.HyDbUtil.getCreateTableSql;
import static com.haley.ormdb.HyDbUtil.getQueryEmptyTableSql;
import static com.haley.ormdb.HyDbUtil.getTableName;

public final class HyDbManager {

    private static HyDbManager dbManager;
    private HyDatabase hyDatabase;

    private HyDbManager() {
        this.hyDatabase = HyDatabase.getInstance();
    }

    public static HyDbManager getInstance() {
        if (dbManager == null) {
            synchronized (HyDbManager.class) {
                if (dbManager == null) {
                    dbManager = new HyDbManager();
                }
            }
        }
        return dbManager;
    }

    /**
     * 创建数据库的同时顺带把表都给建了
     *
     * @param database
     * @param dbName
     */
    public synchronized void createDb(SQLiteDatabase database, String dbName) {
        LogUtil.i("Execute database create ！！！");

        if (database == null) {
            LogUtil.e("database is null! Can not update database!");
            return;
        }

        List<Class> classList = hyDatabase.getTablesByDbName(dbName);

        if (classList == null || classList.size() == 0) {
            LogUtil.e("Update tables is Empty!");
            return;
        }

        for (Class tbClass : classList) {
            autoCreateTable(database, tbClass);
        }

    }

    /**
     * 自动建表
     *
     * @return
     */
    private boolean autoCreateTable(SQLiteDatabase database, Class tbClass) {
        try {
            database.execSQL(getCreateTableSql(tbClass));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 升级表 (sqlite 对删除字段并不友好)
     * <p>
     * Add Collumn： alter table t_user add [column] user_age int default 0
     * <p>
     * ReName Table Name： alter table t_user rename to t_user_new;
     *
     * @param database SQLiteDatabase
     * @param dbName   DatabaseName
     */
    public synchronized void updateDb(SQLiteDatabase database, String dbName) {
        LogUtil.i("Execute database update ！！！");

        if (database == null) {
            LogUtil.e("database is null! Can not update database!");
            return;
        }

        List<Class> classList = hyDatabase.getTablesByDbName(dbName);

        if (classList == null || classList.size() == 0) {
            LogUtil.e("Update tables is Empty!");
            return;
        }

        //遍历一下数据库表和表对应的实体类的字段，查看是否有新增的字段
        List<Field> lists;
        for (Class mclass : classList) {
            //1， 对比原数据库，查找新增的字段
            lists = getNewAddTableColumns(database, mclass);
            //2，将新增的字段插入到数据库中
            if (lists != null && lists.size() > 0) {
                String alerSql;
                for (Field newField : lists) {
                    alerSql = getAlertTableColumnNameSql(getTableName(mclass), newField);
                    if (TextUtils.isEmpty(alerSql)) {
                        LogUtil.w("Get alter table sql error！");
                        continue;
                    }
                    database.execSQL(alerSql);
                }
            }
        }

    }

    /**
     * 获取新增的字段
     *
     * @param sqLiteDatabase
     * @param tbClass
     * @return
     */
    private List<Field> getNewAddTableColumns(SQLiteDatabase sqLiteDatabase, Class tbClass) {
        List<Field> newColumns = new ArrayList<>();
        Field[] fields = tbClass.getDeclaredFields();
        Cursor cursor = sqLiteDatabase.rawQuery(getQueryEmptyTableSql(tbClass), new String[]{});
        if (cursor != null) {
            try {
                String[] columnNames = cursor.getColumnNames();
                if (columnNames != null) {
                    String filedName;
                    for (Field field : fields) {
                        if (!field.isAnnotationPresent(HyField.class)) {
                            continue;
                        }

                        filedName = field.getAnnotation(HyField.class).value();
                        boolean exist = false;

                        for (String columnName : columnNames) {
                            if (columnName.equals(filedName)) {
                                exist = true;
                                break;
                            }
                        }

                        if (!exist) {
                            newColumns.add(field);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                //@TODO 如果获取字段名的时候失败了，是否考虑重试？
                newColumns = null;
            } finally {
                cursor.close();
            }
        }

        LogUtil.i("getNewAddTableColumns newColumns:" + newColumns);

        return newColumns;
    }

}
