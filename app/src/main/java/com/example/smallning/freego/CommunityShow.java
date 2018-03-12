package com.example.smallning.freego;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Smallning on 2018/3/6.
 */

public class CommunityShow implements Serializable{
    private int id;
    private Bitmap portrait = null;
    private String name;
    private String date;
    private String content;
    private int likeNum;
    private int pictureNum;
    private List<Bitmap> pictureList = null;

    public int getId() {
        return id;
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

    public int getPictureNum() {
        return pictureNum;
    }

    public List<Bitmap> getPictureList() {
        return pictureList;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setPictureNum(int pictureNum) {
        this.pictureNum = pictureNum;
    }

    public void setPictureList(List<Bitmap> pictureList) {
        this.pictureList = pictureList;
    }
}
