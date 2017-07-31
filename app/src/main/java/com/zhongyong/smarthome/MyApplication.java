package com.zhongyong.smarthome;

import android.app.Application;

import com.zhongyong.smarthome.utils.MyPreference;


public class MyApplication extends Application {

    public static MyPreference mPreference;
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mPreference = new MyPreference(getApplicationContext());

    }
}
