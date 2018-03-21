package com.example.smallning.freego;

import android.graphics.Bitmap;

/**
 * Created by Smallning on 2018/3/12.
 */

public class Comment {
    private int Id;
    private Bitmap portrait = null;
    private String name;
    private String date;
    private String content;
    private int likeNum;
    private boolean isLike = false;

    public void setId(int id) {
        Id = id;
    }

    public void setPortrait(Bitmap portrait) {
        this.portrait = portrait;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public void setIsLike(boolean like) {
        isLike = like;
    }

    public int getId() {
        return Id;
    }

    public Bitmap getPortrait() {
        return portrait;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public boolean getIsLike() {
        return isLike;
    }
}
