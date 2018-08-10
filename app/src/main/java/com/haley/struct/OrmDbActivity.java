package com.haley.struct;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.haley.struct.bean.User;
import com.haley.struct.ormdb.HyBaseDao;
import com.haley.struct.ormdb.HyDbFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haley on 2018/8/10.
 */

public class OrmDbActivity extends AppCompatActivity {

    private HyBaseDao<User, Integer> baseDao;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ormdb);

        baseDao = HyDbFactory.getInstance().getBaseDao(User.class);
    }

    public void insertOne(View view) {
        User user = new User();
        user.setUserName("ss");
        user.setPassword("pp");
        baseDao.save(user);
    }

    public void insertMore(View view) {
        List<User> list = new ArrayList(100);
        User user;
        for (int i = 0; i < 100; i++) {
            user = new User();
            user.setUserName("name_" + i);
            user.setPassword("password_" + i);
            list.add(user);
        }
        baseDao.save(list);
    }
}
