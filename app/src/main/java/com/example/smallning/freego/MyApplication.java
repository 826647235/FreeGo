package com.example.smallning.freego;

import android.app.Application;
import android.content.Context;

/**
 * Created by Smallning on 2017/9/26.
 */

public class MyApplication extends Application {

    private static Context context;
    private String Account=null;
    private String Password=null;
    private boolean isLogin=false;

    @Override
    public void onCreate() {
        super.onCreate();
        context.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
