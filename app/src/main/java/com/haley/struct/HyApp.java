package com.haley.struct;

import android.app.Application;

import com.haley.ormdb.HyDatabase;
import com.haley.struct.bean.User;

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
                HyDatabase.HyDabaseBuilder
                        .build(this)
                        .setSingleDbConfig("test.db", 2, lists));
    }

    public static HyApp getContext() {
        return app;
    }

}
