package com.wilben.enddesign.entity;

import java.util.ArrayList;

public class Case {
    private String avatar; // 用户头像URL
    private String username; // 标题
    private String description; // 内容
    private ArrayList<String> imageUrls; // 九宫格图片的URL集合

    public Case(String avatar, String username, String description, ArrayList<String> imageUrls) {
        super();
        this.avatar = avatar;
        this.username = username;
        this.description = description;
        this.imageUrls = imageUrls;
    }

    public Case() {
        super();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }


}
