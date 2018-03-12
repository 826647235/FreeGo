package com.example.smallning.freego;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Smallning on 2018/3/12.
 */

public abstract class ClickMethod {

    static void like(final int Id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Id",String.valueOf(Id))
                            .build();
                    Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/like").build();
                    okHttpClient.newCall(request).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void collection(final String name,final int Id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Name",name)
                            .add("Id",String.valueOf(Id))
                            .build();
                    Request request = new Request.Builder().post(requestBody).url("http://106.15.201.54:8080/Freego/collection").build();
                    okHttpClient.newCall(request).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
