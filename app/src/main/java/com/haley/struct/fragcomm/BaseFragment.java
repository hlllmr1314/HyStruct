package com.haley.struct.fragcomm;

import android.support.v4.app.Fragment;

/**
 * Created by haley on 2018/7/26.
 */

public class BaseFragment extends Fragment {

    protected FunctionsManager functionsManager;

    public void setFunctionManager(FunctionsManager functionsManager) {
        this.functionsManager = functionsManager;
    }
}
