package com.haley.struct.bean;

import com.haley.ormdb.HyDbConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * </p>
 * Author: haley$
 * Email : leihuang@ecarx.com.cn
 * Date  : 2018/8/17$.
 * Desc  : UserDbConfig
 * </p>
 */
public class UserDbConfig2 extends HyDbConfig {

    public static String DB_NAME = "test3.db";
    public static int DB_VERSION = 1;
    public static List<Class> DB_TABLES;

    static {
        DB_TABLES = new ArrayList<>();
        DB_TABLES.add(User.class);
    }

    public UserDbConfig2(String dbName, int dbVersion, List<Class> dbTables) {
        super(dbName, dbVersion, dbTables);
    }

    public static UserDbConfig2 getDbConfig() {
        return new UserDbConfig2(DB_NAME, DB_VERSION, DB_TABLES);
    }

}
