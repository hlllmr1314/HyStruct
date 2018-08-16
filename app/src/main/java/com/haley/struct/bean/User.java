package com.haley.struct.bean;


import com.haley.ormdb.annotation.HyField;
import com.haley.ormdb.annotation.HyTable;

import java.util.Arrays;

/**
 * Created by haley on 2018/7/26.
 */

@HyTable("t_user5")
public class User {

    @HyField(value = "user_id", isPrimaryKey = true, isAutoIncrement = true)
    private int userId;

    @HyField(value = "user_name", isNotNull = true)
    private String userName;

    @HyField("user_password")
    private String password;

    @HyField("user_age")
    private Integer userAge;

    @HyField("user_stature")
    private float userStature;

    @HyField("user_photo")
    private byte[] userPhoto;

    @HyField(value = "user_email", defaultValue = "test")
    private String userEmail;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public float getUserStature() {
        return userStature;
    }

    public void setUserStature(float userStature) {
        this.userStature = userStature;
    }

    public byte[] getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userAge=" + userAge +
                ", userStature=" + userStature +
                ", userPhoto=" + Arrays.toString(userPhoto) +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
