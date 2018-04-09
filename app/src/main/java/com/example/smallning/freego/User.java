package com.example.smallning.freego;

/**
 * Created by Smallning on 2018/3/20.
 */

public class User {
    String name;
    String account;
    int age;
    String sex;
    String motto;

    public void setAccount(String account) {
        this.account = account;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getAccount() {
        return account;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getMotto() {
        return motto;
    }
}
