package com.haley.struct.fragcomm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by haley on 2018/7/26.
 */

public class BaseActivity extends AppCompatActivity {

    protected FunctionsManager functionsManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        functionsManager = FunctionsManager.getInstance();
    }

}
