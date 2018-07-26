package com.haley.struct.fragcomm;

import android.text.TextUtils;

import java.security.InvalidParameterException;

/**
 * Created by haley on 2018/7/26.
 */

public class Function {

    /**
     * 这里的functionName必须要唯一
     */
    protected String mFunctionName;

    public Function(String mFunctionName) {

        if(TextUtils.isEmpty(mFunctionName)){
            throw new InvalidParameterException("FunctionName can not empty!");
        }

        this.mFunctionName = mFunctionName;
    }

    public String getFunctionName() {
        return mFunctionName;
    }
}
