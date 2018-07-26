package com.haley.struct.fragcomm;

/**
 * Created by haley on 2018/7/26.
 */

public abstract class FunctionParamAndResult<Result, Param> extends Function {
    public FunctionParamAndResult(String functionName) {
        super(functionName);
    }

    public abstract Result function(Param param);
}
