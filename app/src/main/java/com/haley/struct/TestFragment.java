package com.haley.struct;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haley.struct.fragcomm.BaseFragment;

/**
 * Created by haley on 2018/7/26.
 */

public class TestFragment extends BaseFragment {

    public static final String INTERFACE1 = TestFragment.class.getName() + "NPNR";
    public static final String INTERFACE2 = TestFragment.class.getName() + "OnlyParam";
    public static final String INTERFACE3 = TestFragment.class.getName() + "OnlyResult";
    public static final String INTERFACE4 = TestFragment.class.getName() + "PR";
    private MainActivity mActivity;

    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_content, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        getView().findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                functionsManager.invokeFunction(INTERFACE1);
                functionsManager.invokeFunction(INTERFACE2, "param");
                functionsManager.invokeFunction(INTERFACE3, String.class);
                functionsManager.invokeFunction(INTERFACE4, String.class, "param");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            //进行注册
            mActivity = (MainActivity) context;
            mActivity.setFunctionsForFragment(getTag());
        }
    }
}
