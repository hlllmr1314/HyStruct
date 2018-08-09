package com.haley.struct.bean;

import com.haley.struct.ormdb.annotation.HyField;
import com.haley.struct.ormdb.annotation.HyTable;

/**
 * Created by haley on 2018/7/26.
 */

@HyTable("t_user3")
public class User {

    @HyField(value = "user_id", isPrimaryKey = true, isAutoIncrement = true)
    private Integer userId;

    @HyField(value = "user_name", isNotNull = true)
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
