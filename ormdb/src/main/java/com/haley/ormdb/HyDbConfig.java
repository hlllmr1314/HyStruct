package com.haley.ormdb;

import java.util.List;

/**
 * </p>
 * Author: haley
 * Email : leihuang@ecarx.com.cn
 * Date  : 2018/8/16.
 * Desc  : HyDbConfig
 * </p>
 */

public class HyDbConfig {

    private String dbName;
    private int dbVersion;
    private List<Class> dbTables;


    public HyDbConfig(String dbName, int dbVersion, List<Class> dbTables) {
        this.dbName = dbName;
        this.dbVersion = dbVersion;
        this.dbTables = dbTables;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(int dbVersion) {
        this.dbVersion = dbVersion;
    }

    public List<Class> getDbTables() {
        return dbTables;
    }

    public void setDbTables(List<Class> dbTables) {
        this.dbTables = dbTables;
    }
}
