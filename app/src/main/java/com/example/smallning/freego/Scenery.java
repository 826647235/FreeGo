package com.example.smallning.freego;

import android.graphics.drawable.Drawable;

/**
 * Created by Smallning on 2018/3/16.
 */

public class Scenery {
    private int picture;
    private String province;
    private String url;

    Scenery(String province, int picture, String url) {
        this.province = province;
        this.picture = picture;
        this.url = url;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPicture() {
        return picture;
    }

    public String getProvince() {
        return province;
    }

    public String getUrl() {
        return url;
    }
}
