package com.wilben.enddesign.entity;

public class Work {
    private String imageUrl;
    private String title;
    private int workId;


    public Work() {
    }

    public Work(String imageUrl, String title, int workId) {
        super();
        this.imageUrl = imageUrl;
        this.title = title;
        this.workId = workId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }
}
