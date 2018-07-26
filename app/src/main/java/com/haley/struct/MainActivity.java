package com.haley.struct;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.haley.struct.fragcomm.BaseActivity;
import com.haley.struct.fragcomm.BaseFragment;
import com.haley.struct.fragcomm.FunctionNoParamNoResult;
import com.haley.struct.fragcomm.FunctionOnlyParam;
import com.haley.struct.fragcomm.FunctionOnlyResult;
import com.haley.struct.fragcomm.FunctionParamAndResult;
import com.haley.struct.fragcomm.FunctionsManager;

import static com.haley.struct.TestFragment.INTERFACE2;
import static com.haley.struct.TestFragment.INTERFACE3;
import static com.haley.struct.TestFragment.INTERFACE4;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content, TestFragment.newInstance(), TestFragment.class.getName()).commit();

    }

    @Override
    public FunctionsManager getFunctionsManagerSupport() {
        return functionsManager
                .addFunction(new FunctionNoParamNoResult(TestFragment.INTERFACE1) {
                    @Override
                    public void function() {
                        Toast.makeText(getApplicationContext(), "NPNR", Toast.LENGTH_LONG).show();
                    }
                })
                .addFunction(new FunctionOnlyParam<String>(INTERFACE2) {
                    @Override
                    public void function(String s) {
                        Toast.makeText(getApplicationContext(), "OnlyParam " + s, Toast.LENGTH_LONG).show();
                    }
                })
                .addFunction(new FunctionOnlyResult<String>(INTERFACE3) {
                    @Override
                    public String function() {
                        Toast.makeText(getApplicationContext(), "OnlyResult ", Toast.LENGTH_LONG).show();
                        return "hehe";
                    }
                })
                .addFunction(new FunctionParamAndResult<String, String>(INTERFACE4) {
                    @Override
                    public String function(String s) {
                        Toast.makeText(getApplicationContext(), "ParamAndResult " + s, Toast.LENGTH_LONG).show();
                        return s + " hehe";
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
