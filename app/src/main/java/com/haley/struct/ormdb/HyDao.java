package com.haley.struct.ormdb;

import java.io.Serializable;
import java.util.List;

/**
 * Created by haley on 2018/7/26.
 */

public interface HyDao<T, ID extends Serializable> {

    void close();

    /**
     * 保存单个实例
     *
     * @param var1
     * @return
     */
    Long save(T var1);

    /**
     * 批量保存
     *
     * @param var1
     * @return
     */
    boolean save(Iterable<T> var1);

    /**
     * 查询全部
     *
     * @return
     */
    List<T> findAll();

    /**
     * 通过批量的ID查询
     *
     * @param var1
     * @return
     */
    List<T> findAll(Iterable<ID> var1);

    /**
     * 删除全部
     */
    void deleteAll();

    /**
     * 删除特定的ID的记录
     *
     * @param var1
     */
    void delete(ID var1);

    /**
     * 删除一个实例的记录
     *
     * @param var1
     */
    void delete(T var1);

    /**
     * 删除批量实例记录
     *
     * @param var1
     */
    boolean delete(Iterable<? extends ID> var1);

    /**
     * 删除数据库
     *
     * @return
     */
    boolean dropTable();

    /**
     * 执行SQL语句
     *
     * @param sql
     */
    boolean execSQL(String sql);

    /**
     * 执行SQL语句
     *
     * @param sql
     * @param bindArgs
     */
    boolean execSQL(String sql, Object[] bindArgs);
}
