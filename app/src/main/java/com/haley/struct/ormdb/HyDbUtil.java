package com.haley.struct.ormdb;

import android.util.Log;

import com.haley.struct.LogUtil;
import com.haley.struct.ormdb.annotation.HyField;
import com.haley.struct.ormdb.annotation.HyTable;
import com.haley.struct.ormdb.exception.HyCreateTableException;

import java.lang.reflect.Field;

/**
 * Created by haley on 2018/8/8.
 */

public final class HyDbUtil {

    /**
     * 检查Table是否符合要求
     *
     * @param tbClass
     */
    private static void check(Class tbClass) {
        if (!tbClass.isAnnotationPresent(HyTable.class)) {
            throw new HyCreateTableException("Please add @HyTable to the entity class of the table!");
        }
    }

    /**
     * 建表语句
     *
     * @param tbClass
     * @return
     */
    public static String getCreateTableSql(Class tbClass) {

        String tableName = getTableName(tbClass);

        StringBuffer stringBuffer = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
        stringBuffer.append(tableName);
        stringBuffer.append(" (");

        for (Field field : tbClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(HyField.class)) {
                if (field.getType() == String.class) {
                    stringBuffer.append(field.getAnnotation(HyField.class).value() + " TEXT,");
                } else if (field.getType() == Integer.class) {
                    stringBuffer.append(field.getAnnotation(HyField.class).value() + " INTEGER,");
                } else if (field.getType() == Double.class) {
                    stringBuffer.append(field.getAnnotation(HyField.class).value() + " DOUBLE,");
                } else if (field.getType() == Long.class) {
                    stringBuffer.append(field.getAnnotation(HyField.class).value() + " BIGINT,");
                } else if (field.getType() == byte[].class) {
                    stringBuffer.append(field.getAnnotation(HyField.class).value() + " BLOB,");
                } else if (field.getType() == Float.class) {
                    stringBuffer.append(field.getAnnotation(HyField.class).value() + " FLOAT,");
                } else {
                    LogUtil.w(field.getType() + "类型不支持");
                }
            }
        }

        if (stringBuffer.lastIndexOf(",") > 0) {
            stringBuffer.deleteCharAt(stringBuffer.lastIndexOf(","));
        }

        stringBuffer.append(")");

        String sql = stringBuffer.toString();
        return sql;
    }

    /**
     * 获取表名
     *
     * @param tbClass
     * @return
     */
    public static String getTableName(Class tbClass) {

        check(tbClass);

        HyTable annotation = (HyTable) tbClass.getAnnotation(HyTable.class);
        String tableName = annotation.value();

        return tableName;
    }

    /**
     * 查询一次空表的SQL语句
     *
     * @param tbClass
     * @return
     */
    public static String getQueryEmptyTableSql(Class tbClass) {

        check(tbClass);

        StringBuffer stringBuffer = new StringBuffer("SELECT * FROM ");
        stringBuffer.append(getTableName(tbClass));
        stringBuffer.append(" LIMIT 0");

        return stringBuffer.toString();
    }


}
