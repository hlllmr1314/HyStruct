package com.haley.struct.ormdb;

import java.util.Map;

/**
 * Created by haley on 2018/7/31.
 */

public class HyDbProvider {

    private static HyDbProvider mInstance;

    private HyDbProvider() {

    }

    public static HyDbProvider getInstance() {
        if (mInstance == null) {
            synchronized (HyDbProvider.class) {
                if (mInstance == null) {
                    mInstance = new HyDbProvider();
                }
            }
        }
        return mInstance;
    }

    public static class Builder {

        Map<String, HyDbHelper> dbHelperMap;

        private Builder(String dbName) {

        }

    }

}
