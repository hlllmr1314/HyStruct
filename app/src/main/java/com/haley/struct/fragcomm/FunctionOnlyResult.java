package com.haley.struct.fragcomm;

/**
 * Created by haley on 2018/7/26.
 */

public abstract class FunctionOnlyResult<Result> extends Function {
    public FunctionOnlyResult(String functionName) {
        super(functionName);
    }

    public abstract Result function();
}
