package com.zhongyong.smarthome;

import android.app.Application;

import com.zhongyong.smarthome.utils.MyPreference;


public class MyApplication extends Application {

	public static MyPreference mPreference;

	@Override
	public void onCreate() {
		super.onCreate();

		mPreference = new MyPreference(getApplicationContext());

	}
}
