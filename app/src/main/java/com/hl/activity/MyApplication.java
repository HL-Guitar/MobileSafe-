package com.hl.activity;

import android.app.Application;

import org.xutils.x;

/**
 * Created by UTT on 2018/6/29.
 */

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        //初始化XUtils
        x.Ext.init(this);
        //设置debug模式
        x.Ext.setDebug(true);
    }

}
