package com.haley.ormdb;

import org.junit.Test;

import java.lang.reflect.Field;

import static com.haley.ormdb.HyDbUtil.getAlertTableColumnNameSql;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void validAlertTableColumnNameSql() {
    }
}