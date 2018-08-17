package com.haley.ormdb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.haley.base.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * </p>
 * Author: haley
 * Email : leihuang@ecarx.com.cn
 * Date  : 2018/8/16.
 * Desc  : 框架初始化的核心类
 * </p>
 */

public final class HyDatabase {

    private static HyDatabase mInstance;
    private HyDatabaseBuilder builder;

    protected List<HyDbConfig> dbConfigs;
    protected Map<String, HyDbHelper> dbHelperMaps;

    private HyDatabase() {
        this.dbHelperMaps = new ArrayMap<>();
    }

    public static HyDatabase getInstance() {

        if (mInstance == null) {
            synchronized (HyDatabase.class) {
                if (mInstance == null) {
                    mInstance = new HyDatabase();
                }
            }
        }
        return mInstance;
    }

    public static void init(@NonNull HyDatabaseBuilder builder) {
        if (builder == null) {
            throw new NullPointerException("HyDatabaseBuilder builder cannot be null !");
        }

        getInstance();

        mInstance.builder = builder;
        mInstance.dbConfigs = builder.dbConfigs;

        mInstance.checkInit();
        mInstance.initDbHelper();
    }

    private void checkInit() {
        if (builder.context == null) {
            throw new NullPointerException("Do the initialization HyDb.init() method first in Application onCreate()!");
        }
    }

    private void initDbHelper() {
        if (dbConfigs == null || dbConfigs.size() == 0) {
            LogUtil.w("initDbHelper fail!");
            return;
        }

        for (HyDbConfig config : dbConfigs) {
            dbHelperMaps.put(config.getDbName(), new HyDbHelper(config.getDbName(), config.getDbVersion()));
        }

    }

    /**
     * 对外提供Context
     *
     * @return
     */
    public static Context getContext() {
        return getInstance().builder.context;
    }

    /**
     * 返回指定数据只能够对应的表
     *
     * @param dbName
     * @return
     */
    public List<Class> getTablesByDbName(String dbName) {
        for (HyDbConfig config : dbConfigs) {
            if (!TextUtils.isEmpty(dbName) && dbName.equals(config.getDbName())) {
                return config.getDbTables();
            }
        }
        return null;
    }

    /**
     * 根据DbName获取DbHelper
     *
     * @param dbName
     * @return
     */
    public HyDbHelper getDbHelperByDbName(String dbName) {
        if (dbHelperMaps == null) {
            return null;
        }

        return dbHelperMaps.get(dbName);
    }

    /**
     * 如果就一个数据库，则无需传查找默认的就可以了，如果由多个数据库，也不做处理，因为我也不知道你想操作哪个数据库
     *
     * @return
     */
    public HyDbHelper getSingleDbHelper() {
        if (dbHelperMaps == null) {
            LogUtil.e("Not execute setSingleDbConfig（） first！");
            return null;
        }

        if (dbHelperMaps.size() != 1) {
            LogUtil.e("Multiple selections exist for multiple databases!");
            return null;
        }

        Iterator<HyDbHelper> iterator = dbHelperMaps.values().iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public static class HyDatabaseBuilder {

        protected Context context;
        protected List<HyDbConfig> dbConfigs;

        private HyDatabaseBuilder(Context context) {
            this.context = context;
            this.dbConfigs = new ArrayList<>();
        }

        public static HyDatabaseBuilder build(Context context) {
            return new HyDatabaseBuilder(context);
        }

        public HyDatabaseBuilder setSingleDbConfig(String dbName, int dbVersion, List<Class> dbTables) {
            dbConfigs.clear();
            dbConfigs.add(new HyDbConfig(dbName, dbVersion, dbTables));
            return this;
        }

        public HyDatabaseBuilder setSingleDbConfig(HyDbConfig dbConfig) {
            dbConfigs.clear();
            dbConfigs.add(dbConfig);
            return this;
        }

        public HyDatabaseBuilder setMultiDbConfig(ProviderDbConfigCallback multiDbConfig) {
            dbConfigs.clear();
            dbConfigs.addAll(multiDbConfig.getDbConfigs());
            return this;
        }

    }

    public interface ProviderDbConfigCallback {
        List<HyDbConfig> getDbConfigs();
    }

}
