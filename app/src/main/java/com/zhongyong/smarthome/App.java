package com.zhongyong.smarthome;

import android.app.Application;

import com.fbee.zllctl.DeviceInfo;
import com.fbee.zllctl.GatewayInfo;
import com.fbee.zllctl.Serial;
import com.umeng.socialize.PlatformConfig;
import com.zhongyong.jamod.model.User;
import com.zhongyong.smarthome.utils.MyPreference;

import java.util.ArrayList;


public class App extends Application {
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

    private static App instance;

    private static User user;

    static {
        //微信
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        //QQ
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        //微博
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");

    }

    public static App getInstance() {
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

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        App.user = user;
    }

    public void exit() {
        if (mSerial != null)
            mSerial.releaseSource();
    }
}
