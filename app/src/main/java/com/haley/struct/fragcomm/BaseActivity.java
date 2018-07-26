package com.haley.struct.fragcomm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.haley.struct.TestFragment;

import static com.haley.struct.TestFragment.INTERFACE2;
import static com.haley.struct.TestFragment.INTERFACE3;
import static com.haley.struct.TestFragment.INTERFACE4;

/**
 * Created by haley on 2018/7/26.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected FunctionsManager functionsManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        functionsManager = FunctionsManager.getInstance();
    }

    public void setFunctionsForFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        BaseFragment fragment = (BaseFragment) fragmentManager.findFragmentByTag(tag);
        fragment.setFunctionManager(getFunctionsManagerSupport());
    }

    public abstract FunctionsManager getFunctionsManagerSupport();

}
