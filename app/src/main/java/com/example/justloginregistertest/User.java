package com.example.justloginregistertest;

public class User {
    private String name;            //用户名
    private String truename;            //本名
    private String password;        //密码
    public User(String name, String password,String truename) {
        this.name = name;
        this.truename=truename;
        this.password = password;
    }
    public String getName() {
        return name;
    }

    public String getTrueName() {
        return truename;
    }

    public String getPassword() {
        return password;
    }
    @Override

    public String toString() {
        return "用户信息{" +
                "name='" + name + '\'' +", truename='" + password + '\''+
                ", password='" + truename + '\'' +
                '}';
    }
}

