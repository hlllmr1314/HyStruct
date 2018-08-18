package com.haley.base;

/**
 * </p>
 * Author: haley
 * Email : leihuang@ecarx.com.cn
 * Date  : 2018/8/18
 * Desc  : HyStruct
 * </p>
 */
public interface ILog {

    void i(String tag,String message);


    void d(String tag,String message);


    void w(String tag,String message);

    void e(String tag,String message);
}
