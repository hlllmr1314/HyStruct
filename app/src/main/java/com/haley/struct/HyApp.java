package com.haley.struct;

import android.app.Application;

/**
 * Created by haley on 2018/8/8.
 */

public class HyApp extends Application {

    private static HyApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }


    public static HyApp getContext() {
        return app;
    }

}
