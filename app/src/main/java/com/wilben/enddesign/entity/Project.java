package com.wilben.enddesign.entity;

import java.util.ArrayList;

public class Project {
    private String image; // 封面
    private String title; // 标题
    private String time; // 时间
    private String username;//设计师
    private int workId;
    private String description;
    private ArrayList<String> imageUrls; // 九宫格图片的URL集合
    private int state;

    public Project(String image, String title, String time, String username, int workId, String description
            , ArrayList<String> imageUrls, int state) {
        super();
        this.image = image;
        this.title = title;
        this.time = time;
        this.username = username;
        this.workId = workId;
        this.description = description;
        this.imageUrls = imageUrls;
        this.state = state;
    }

    public Project() {
        super();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
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
