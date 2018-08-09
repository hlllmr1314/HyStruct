package com.haley.struct.bean;

import com.haley.struct.ormdb.annotation.HyField;
import com.haley.struct.ormdb.annotation.HyTable;

/**
 * Created by haley on 2018/7/26.
 */

@HyTable("t_user")
public class User {

    @HyField("user_name")
    private String userName;

    @HyField("user_password")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
