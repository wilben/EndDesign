package com.wilben.enddesign.entity;

public class Designer {

    private String username;
    private String password;
    private String sex;
    private String age;
    private int role;
    private String realname;
    private String avatar;
    private String concept;
    private String motto;
    private String style;
    private String work;
    private String period;
    private String area;

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

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

    public Designer(String username, String password, String sex, String realname, String age, String avatar, int role, String concept, String motto, String style, String work, String period, String area) {
        super();
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.age = age;
        this.realname = realname;
        this.avatar = avatar;
        this.role = role;
        this.concept = concept;
        this.motto = motto;
        this.style = style;
        this.work = work;
        this.period = period;
        this.area = area;
    }

    public Designer() {
        super();
        // TODO Auto-generated constructor stub
    }


}
