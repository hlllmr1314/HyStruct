package com.haley.ormdb;

import android.text.TextUtils;

import com.haley.base.LogUtil;
import com.haley.ormdb.annotation.HyField;
import com.haley.ormdb.annotation.HyTable;
import com.haley.ormdb.exception.HyCreateTableException;

import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * </p>
 * Author: haley
 * Email : leihuang@ecarx.com.cn
 * Date  : 2018/8/17.
 * Desc  : HyDbUtil 处理数据库的工具类
 * </p>
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

        HyField annotation;
        for (Field field : tbClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(HyField.class)) {
                annotation = field.getAnnotation(HyField.class);
                if (field.getType() == String.class) {
                    stringBuffer.append(annotation.value() + " TEXT" + isPrimaryKey(annotation) + isAutoIncrement(annotation) + isNotNull(annotation) + ",");
                } else if (field.getType() == Integer.class || field.getType() == int.class) {
                    stringBuffer.append(annotation.value() + " INTEGER" + isPrimaryKey(annotation) + isAutoIncrement(annotation) + isNotNull(annotation) + ",");
                } else if (field.getType() == Double.class || field.getType() == double.class) {
                    stringBuffer.append(annotation.value() + " DOUBLE" + isPrimaryKey(annotation) + isAutoIncrement(annotation) + isNotNull(annotation) + ",");
                } else if (field.getType() == Long.class || field.getType() == long.class) {
                    stringBuffer.append(annotation.value() + " BIGINT" + isPrimaryKey(annotation) + isAutoIncrement(annotation) + isNotNull(annotation) + ",");
                } else if (field.getType() == Float.class || field.getType() == float.class) {
                    stringBuffer.append(annotation.value() + " FLOAT" + isPrimaryKey(annotation) + isAutoIncrement(annotation) + isNotNull(annotation) + ",");
                } else if (field.getType() == byte[].class) {
                    stringBuffer.append(annotation.value() + " BLOB" + isPrimaryKey(annotation) + isAutoIncrement(annotation) + isNotNull(annotation) + ",");
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
        System.out.print("getCreateTableSql:" + sql);
        return sql;
    }

    /**
     * 判断是否为主键，并返回对应SQL标示
     * @param annotation
     * @return
     */
    public static String isPrimaryKey(HyField annotation) {
        return annotation.isPrimaryKey() ? " PRIMARY KEY " : "";
    }

    /**
     * 获取主键
     * @param tbClass
     * @return
     */
    public static String getPrimaryKeyColumnName(Class tbClass) {

        check(tbClass);

        Field[] fields = tbClass.getDeclaredFields();
        HyField annotation;
        for (Field field : fields) {
            if (field.isAnnotationPresent(HyField.class)) {
                annotation = field.getAnnotation(HyField.class);
                if (annotation.isPrimaryKey()) {
                    return annotation.value();
                }
            }
        }

        return null;
    }

    public static String isNotNull(HyField annotation) {
        return annotation.isNotNull() ? " NOT NULL " : "";
    }

    public static String isAutoIncrement(HyField annotation) {
        return annotation.isAutoIncrement() ? " AUTOINCREMENT " : "";
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

        System.out.print("getTableName:" + tableName);

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

        System.out.print("getQueryEmptyTableSql:" + stringBuffer.toString());

        return stringBuffer.toString();
    }

    /**
     * 获取查全表的SQL语句
     *
     * @param tbClass
     * @return
     */
    public static String getQueryAllSql(Class tbClass) {
        check(tbClass);

        StringBuffer stringBuffer = new StringBuffer("SELECT * FROM ");
        stringBuffer.append(getTableName(tbClass));

        System.out.print("getQueryAllSql:" + stringBuffer.toString());

        return stringBuffer.toString();
    }

    /**
     * 获取根据批量Ids查询的SQL语句
     *
     * @param tbClass
     * @param ids
     * @return
     */
    public static String getQueryByIdsSql(Class tbClass, Iterable ids) {

        String sql = getQueryAllSql(tbClass);

        //如果传入的参数为null or 列表李敏是空的则直接返回查全表的SQL语句
        if (ids == null || !ids.iterator().hasNext()) {
            return sql;
        }

        StringBuffer stringBuffer = new StringBuffer(sql);
        stringBuffer.append(" WHERE ");
        stringBuffer.append(getPrimaryKeyColumnName(tbClass));
        stringBuffer.append(" IN (");
        Iterator iterator = ids.iterator();
        while (iterator.hasNext()) {
            stringBuffer.append("?,");
            iterator.next();
        }

        int andCharacterIndex = stringBuffer.lastIndexOf(",");
        if (andCharacterIndex > 0) {
            stringBuffer.deleteCharAt(andCharacterIndex);
        }
        stringBuffer.append(")");

        System.out.print("getQueryByIdsSql:" + stringBuffer.toString());

        return stringBuffer.toString();
    }

    /**
     * 检查是否是默认值
     * @param value
     * @return
     */
    public static boolean checkIsDefalutValue(Object value) {
        if (value instanceof String) {
            return TextUtils.isEmpty((String) value);
        } else if (value instanceof Integer) {
            return (Integer) value == 0;
        } else if (value instanceof Double) {
            return (Double) value == 0;
        } else if (value instanceof Long) {
            return (Long) value == 0;
        } else if (value instanceof Float) {
            return (Float) value == 0;
        } else if (value instanceof byte[]) {
            return value == null;
        }

        return false;
    }

    /**
     * 获取删表的SQL语句
     * @param tbClass
     * @return
     */
    public static String getDropTableSql(Class tbClass) {

        check(tbClass);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("DROP TABLE ");
        stringBuffer.append(getTableName(tbClass));

        System.out.print("getDropTableSql:" + stringBuffer.toString());

        return stringBuffer.toString();
    }

    /**
     * 获取新增字段的SQL语句
     * <p>
     * 例如:alter table t_user add [column] user_age int default 0
     *
     * @param tableName 表名
     * @param field     字段对应属性的Field
     * @return
     */
    public static String getAlertTableColumnNameSql(String tableName, Field field) {

        if (!field.isAnnotationPresent(HyField.class)) {
            return null;
        }

        HyField hyField = field.getAnnotation(HyField.class);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("ALTER TABLE " + tableName);
        stringBuffer.append(" ADD " + hyField.value());
        stringBuffer.append(" " + getColumnNameType(field));
        stringBuffer.append(" DEFAULT " + hyField.defaultValue());

        System.out.print("getAlertTableColumnNameSql:" + stringBuffer.toString());

        return stringBuffer.toString();
    }

    /**
     * 获取
     *
     * @param field
     * @return
     */
    public static String getColumnNameType(Field field) {
        if (field.isAnnotationPresent(HyField.class)) {
            if (field.getType() == String.class) {
                return "TEXT";
            } else if (field.getType() == Integer.class || field.getType() == int.class) {
                return "INTEGER";
            } else if (field.getType() == Double.class || field.getType() == double.class) {
                return "DOUBLE";
            } else if (field.getType() == Long.class || field.getType() == long.class) {
                return "BIGINT";
            } else if (field.getType() == Float.class || field.getType() == float.class) {
                return "FLOAT";
            } else if (field.getType() == byte[].class) {
                return "BLOB";
            }
        }

        return null;

    }

}
