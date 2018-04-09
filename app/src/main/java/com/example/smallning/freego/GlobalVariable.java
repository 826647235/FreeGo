package com.example.smallning.freego;

import android.app.Application;

import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by Smallning on 2018/3/10.
 */

public class GlobalVariable extends Application {
    private String Account;
    private String Name;

    public void setAccount(String account) {
        Account = account;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAccount() {
        return Account;
    }

    public String getName() {
        return Name;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        QbSdk.initX5Environment(this,null);
    }
}
