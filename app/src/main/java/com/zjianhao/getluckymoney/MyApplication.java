package com.zjianhao.getluckymoney;

import android.app.Application;

import com.zjianhao.bean.App;

/**
 * Created by zjianhao on 15-12-13.
 */
public class MyApplication extends Application {
    private App app;

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
