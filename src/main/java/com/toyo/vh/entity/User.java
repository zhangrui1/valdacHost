package com.toyo.vh.entity;

import java.io.Serializable;

/**
 * Created by zhangrui on 14/11/14.
 * userエンティティ
 */
public class User implements Serializable {

    public String userid;
    public String password;
    public String username;
    public String kengen;
    public String profile;
    public String department;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKengen() {
        return kengen;
    }

    public void setKengen(String kengen) {
        this.kengen = kengen;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}

