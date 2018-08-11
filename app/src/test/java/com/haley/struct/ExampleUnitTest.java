package com.haley.struct;

import com.haley.struct.bean.User;
import com.haley.struct.ormdb.HyDbUtil;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void validCreatTableSql() throws Exception {

        String sql = HyDbUtil.getCreateTableSql(User.class);
        System.out.print("sql:" + sql);
        assertNotNull(sql);
    }

    @Test
    public void validQueryEmptyTableSql() throws Exception {

        String sql = HyDbUtil.getQueryEmptyTableSql(User.class);
        System.out.print("sql:" + sql);
        assertNotNull(sql);
    }

    @Test
    public void validSelectAllSql() throws Exception {

        String sql = HyDbUtil.getQueryAllSql(User.class);
        System.out.print("sql:" + sql);
        assertNotNull(sql);
    }

    @Test
    public void validQueryByIdsSql() throws Exception {

        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);
        ids.add(4);

        String sql = HyDbUtil.getQueryByIdsSql(User.class, ids);
        System.out.print("sql:" + sql);
        assertNotNull(sql);
    }
}