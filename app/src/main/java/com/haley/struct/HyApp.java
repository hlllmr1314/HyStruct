package com.haley.struct;

import android.app.Application;

import com.haley.ormdb.HyDatabase;
import com.haley.ormdb.HyDbConfig;
import com.haley.struct.bean.User;
import com.haley.struct.bean.UserDbConfig;
import com.haley.struct.bean.UserDbConfig2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haley on 2018/8/8.
 */

public class HyApp extends Application {

    private static HyApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        List<Class> lists = new ArrayList<>();
        lists.add(User.class);
        HyDatabase.init(
                HyDatabase.HyDatabaseBuilder
                        .build(this)
//                        .setSingleDbConfig(UserDbConfig.getDbConfig())
                        .setMultiDbConfig(new HyDatabase.ProviderDbConfigCallback() {
                            @Override
                            public List<HyDbConfig> getDbConfigs() {
                                List<HyDbConfig> configList = new ArrayList<>();
                                configList.add(UserDbConfig.getDbConfig());
                                configList.add(UserDbConfig2.getDbConfig());
                                return configList;
                            }
                        })
        );
    }

    public static HyApp getContext() {
        return app;
    }

}
