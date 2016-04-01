package com.wilben.enddesign.entity;

public class User {

    private String username;
    private String password;
    private String sex;
    private String age;
    private int role;
    private String realname;
    private String avatar;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public User(String username, String password, String sex, String realname, String age, String avatar, int role) {
        super();
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.age = age;
        this.realname = realname;
        this.avatar = avatar;
        this.role = role;
    }

    public User() {
        super();
        // TODO Auto-generated constructor stub
    }


}
