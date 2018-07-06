package com.zhongyong.smarthome;

import android.app.Application;
import android.app.Notification;
import android.content.Context;

import com.fbee.zllctl.DeviceInfo;
import com.fbee.zllctl.GatewayInfo;
import com.fbee.zllctl.Serial;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.socialize.PlatformConfig;
import com.zhongyong.jamod.model.User;
import com.zhongyong.smarthome.utils.MyPreference;

import java.util.ArrayList;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;


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

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
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
        //JPush初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        setStyleBasic();
    }

    /**
     * 设置通知提示方式 - 基础属性
     */
    private void setStyleBasic() {
        //服务端的build_id,一定要和客户端的保持一致。
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.mipmap.logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(3, builder);
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
