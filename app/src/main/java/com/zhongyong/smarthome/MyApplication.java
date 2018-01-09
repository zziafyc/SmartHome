package com.zhongyong.smarthome;

import android.app.Application;

import com.fbee.zllctl.DeviceInfo;
import com.fbee.zllctl.GatewayInfo;
import com.fbee.zllctl.Serial;
import com.zhongyong.smarthome.utils.MyPreference;

import java.util.ArrayList;


public class MyApplication extends Application {
    //飞比设备部分
    public static Serial mSerial;
    public static ArrayList<DeviceInfo> deviceInfos = new ArrayList<>();
    public static ArrayList<DeviceInfo> switchDeviceInfos = new ArrayList<>();
    public static ArrayList<DeviceInfo> thtbDeviceInfos = new ArrayList<>();
    public static ArrayList<DeviceInfo> doorDeviceInfos = new ArrayList<>();
    public static ArrayList<DeviceInfo> gasDeviceInfos = new ArrayList<>();
    public static ArrayList<DeviceInfo> cogasDeviceInfos = new ArrayList<>();
    public static ArrayList<DeviceInfo> smokeDeviceInfos = new ArrayList<>();
    public static GatewayInfo gatewayInfo;
    //主题设置
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
        if (mSerial == null) {
            mSerial = new Serial();
        }
        mSerial.setmContext(getApplicationContext());
    }
    public void exit() {
        if (mSerial != null)
            mSerial.releaseSource();
    }
}
