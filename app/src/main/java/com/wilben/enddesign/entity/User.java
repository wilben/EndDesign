package com.wilben.enddesign.entity;

/**
 * 用户--模型
 */
public class User {

    private String username;
    private String password;
    private String sex;
    private String age;
    private String avatar;
    private String realname;
    private int role;
    private String style;

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

    public User(String username, String password, String sex, String age,
                String avatar, String realname, int role, String style) {
        super();

        this.username = username;
        this.password = password;
        this.sex = sex;
        this.age = age;
        this.avatar = avatar;
        this.realname = realname;
        this.role = role;
        this.style = style;
    }

    public User() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

}
