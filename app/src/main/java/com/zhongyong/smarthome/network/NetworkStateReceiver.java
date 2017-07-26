package com.zhongyong.smarthome.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;

public class NetworkStateReceiver extends BroadcastReceiver {

    private static final String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private static BroadcastReceiver self;
    private boolean isNetworkAvailable = false;
    private static ArrayList<ConnectionObserver> observers = new ArrayList<>();

    public NetworkStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ANDROID_NET_CHANGE_ACTION.equalsIgnoreCase(intent.getAction())) {
            if (!NetworkUtils.isNetworkAvailable(context)) {
                isNetworkAvailable = false;
            } else {
                isNetworkAvailable = true;
            }
            notifyObserver();
        }
    }

    private void notifyObserver() {
        if (observers.isEmpty()) {
            return;
        }
        for (ConnectionObserver co : observers) {
            if (co != null) {
                if (isNetworkAvailable) {
                    co.onNetConnected();
                } else {
                    co.onNetDisconnect();
                }
            }
        }
    }

    public static void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        context.getApplicationContext().registerReceiver(getReceiver(), filter);
    }

    public static void removerReceiver(Context context) {
        if (self != null) {
            try {
                context.getApplicationContext().unregisterReceiver(self);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static BroadcastReceiver getReceiver() {
        if (self == null) {
            self = new NetworkStateReceiver();
        }
        return self;
    }

    public static void registerObserver(ConnectionObserver observer,Context context) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(observer);
        //下面这一句是fyc添加的，参数context也是fyc添加的
        registerReceiver(context);

    }

    public static void removeObserver(ConnectionObserver observer) {
        if (observers != null && observers.contains(observer)) {
            observers.remove(observer);
        }
    }
}
