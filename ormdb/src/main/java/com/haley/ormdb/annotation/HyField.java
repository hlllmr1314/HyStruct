package com.haley.ormdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by haley on 2018/7/26.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HyField {
    /**
     * 字段名
     *
     * @return
     */
    String value();

    /**
     * 是否为主键
     *
     * @return
     */
    boolean isPrimaryKey() default false;

    /**
     * 自增ID
     *
     * @return
     */
    boolean isAutoIncrement() default false;

    /**
     * 是否可以为null
     *
     * @return true:不可以为null false:可以为null
     */
    boolean isNotNull() default false;

    /**
     * 字段默认值
     *
     * @return
     */
    String defaultValue() default "";
}
