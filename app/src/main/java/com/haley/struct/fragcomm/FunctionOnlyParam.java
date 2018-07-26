package com.haley.struct.fragcomm;

/**
 * Created by haley on 2018/7/26.
 */

public abstract class FunctionOnlyParam<Param> extends Function {
    public FunctionOnlyParam(String functionName) {
        super(functionName);
    }

    public abstract void function(Param param);
}
