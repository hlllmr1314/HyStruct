package com.haley.struct;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.haley.ormdb.HyBaseDao;
import com.haley.ormdb.HyDbFactory;
import com.haley.struct.bean.User;
import com.haley.struct.bean.UserDbConfig;

import java.util.ArrayList;
import java.util.List;

import static com.haley.struct.BitmapUtils.getBitmapBytes;

/**
 * Created by haley on 2018/8/10.
 */

public class OrmDbActivity extends AppCompatActivity {

    private HyBaseDao<User, Integer> baseDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ormdb);

        baseDao = HyDbFactory.getInstance().getBaseDao(UserDbConfig.DB_NAME, User.class);
    }

    public void insertOne(View view) {
        User user = new User();
        user.setUserId(5);
        user.setUserName("sss");
        user.setPassword("ppp");
        user.setUserAge(30);
        user.setUserStature(1.8f);
        user.setUserEmail("leihuang@aa.com");

        user.setUserPhoto(getBitmapBytes(this, R.mipmap.ic_launcher));
        baseDao.save(user);
    }

    public void insertMore(View view) {
        List<User> list = new ArrayList(100);
        User user;
        for (int i = 0; i < 100; i++) {
            user = new User();
            user.setUserName("name_" + i);
            user.setPassword("password_" + i);
            user.setUserAge(i);
            user.setUserStature(i);
            list.add(user);
        }
        baseDao.save(list);
    }

    public void findAll(View view) {
        List<User> lists = baseDao.findAll();
        LogUtil.w("findAll:" + lists.toString());

        if (lists.size() > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(lists.get(0).getUserPhoto(), 0, lists.get(0).getUserPhoto().length);
            ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bitmap);
        }
    }

    public void findAllByIds(View view) {
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);

        List<User> lists = baseDao.findAll(ids);
        LogUtil.w("findAllByIds:" + lists.size() + lists.toString());
    }

    public void deletAll(View view) {
        baseDao.deleteAll();
    }

    public void deleteById(View view) {
        baseDao.delete(4);
    }

    public void deleteByIds(View view) {
        List<Integer> ids = new ArrayList<>();
        ids.add(6);
        ids.add(7);
        boolean result = baseDao.delete(ids);
        LogUtil.w("deleteByIds:" + result);
    }

    public void dropTable(View view) {
//        baseDao.execSQL("DROP TABLE t_user5");
        baseDao.dropTable();
    }
}
