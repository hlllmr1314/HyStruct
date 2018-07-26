package com.haley.struct.fragcomm;

/**
 * Created by haley on 2018/7/26.
 */

import android.text.TextUtils;

import com.haley.struct.LogUtil;

import java.util.HashMap;

/**
 * 用来管理各种Functions
 */
public class FunctionsManager {

    private static FunctionsManager mInstance;

    private FunctionsManager() {
        this.noParamNoResultHashMaps = new HashMap<>();
        this.onlyParamHashMap = new HashMap<>();
        this.onlyResultHashMap = new HashMap<>();
        this.paramAndResultHashMap = new HashMap<>();
    }

    public static FunctionsManager getInstance() {
        if (mInstance == null) {
            synchronized (FunctionsManager.class) {
                if (mInstance == null) {
                    mInstance = new FunctionsManager();
                }
            }
        }

        return mInstance;
    }

    private HashMap<String, FunctionNoParamNoResult> noParamNoResultHashMaps;
    private HashMap<String, FunctionOnlyParam> onlyParamHashMap;
    private HashMap<String, FunctionOnlyResult> onlyResultHashMap;
    private HashMap<String, FunctionParamAndResult> paramAndResultHashMap;

    /**
     * 添加Function对象
     *
     * @param function
     * @return 返回值设计成对象本身，外面可以进行一个链式的调用
     */
    public FunctionsManager addFunction(Function function) {
        if (function instanceof FunctionNoParamNoResult) {
            noParamNoResultHashMaps.put(function.getFunctionName(), (FunctionNoParamNoResult) function);
        } else if (function instanceof FunctionOnlyParam) {
            onlyParamHashMap.put(function.getFunctionName(), (FunctionOnlyParam) function);
        } else if (function instanceof FunctionOnlyResult) {
            onlyResultHashMap.put(function.getFunctionName(), (FunctionOnlyResult) function);
        } else if (function instanceof FunctionParamAndResult) {
            paramAndResultHashMap.put(function.getFunctionName(), (FunctionParamAndResult) function);
        }
        return this;
    }


    /**
     * 无参无返回值
     *
     * @param functionName
     */
    public boolean invokeFunction(String functionName) {

        if (TextUtils.isEmpty(functionName)) {
            return false;
        }

        FunctionNoParamNoResult function = noParamNoResultHashMaps.get(functionName);
        if (function != null) {
            function.function();
            return true;
        } else {
            LogUtil.w("Not find functionName function");
        }

        return false;
    }


    /**
     * 有参无返回值
     * @param functionName
     * @param param
     * @param <Param>
     * @return
     */
    public <Param> boolean invokeFunction(String functionName, Param param) {
        if (TextUtils.isEmpty(functionName)) {
            return false;
        }

        FunctionOnlyParam function = onlyParamHashMap.get(functionName);
        if (function != null) {
            function.function(param);
            return true;
        } else {
            LogUtil.w("Not find functionName function");
        }

        return false;
    }

    /**
     * 无参有返回值
     * @param functionName
     * @param resultClass
     * @param <Result>
     * @return
     */
    public <Result> Result invokeFunction(String functionName, Class<Result> resultClass) {
        if (TextUtils.isEmpty(functionName)) {
            return null;
        }

        FunctionOnlyResult function = onlyResultHashMap.get(functionName);
        if (function != null) {
            if (resultClass != null) {
                //将function返回的类型转成Result类型
                return resultClass.cast(function.function());
            } else {
                return (Result) function.function();
            }
        }

        return null;
    }

    /**
     * 有参有返回值
     * @param functionName
     * @param resultClass
     * @param param
     * @param <Result>
     * @param <Param>
     * @return
     */
    public <Result, Param> Result invokeFunction(String functionName, Class<Result> resultClass, Param param) {
        if (TextUtils.isEmpty(functionName)) {
            return null;
        }

        FunctionParamAndResult function = paramAndResultHashMap.get(functionName);

        if (function != null) {
            if (resultClass != null) {
                return resultClass.cast(function.function(param));
            } else {
                return (Result) function.function(param);
            }
        }

        return null;
    }
}
