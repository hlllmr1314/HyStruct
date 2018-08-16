package com.haley.ormdb.exception;

/**
 * Created by haley on 2018/8/8.
 */

public class HyCreateTableException extends RuntimeException {

    public HyCreateTableException() {
        super();
    }

    public HyCreateTableException(String message) {
        super(message);
    }
}
