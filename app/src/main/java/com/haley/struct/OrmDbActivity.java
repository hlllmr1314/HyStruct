package com.haley.struct;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.haley.struct.bean.User;
import com.haley.struct.ormdb.HyBaseDao;
import com.haley.struct.ormdb.HyDbFactory;

import java.io.ByteArrayOutputStream;
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
        user.setUserAge(29);
        user.setUserStature(1.7f);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();

        user.setUserPhoto(bitmapdata);
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
}
