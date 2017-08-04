package com.zhongyong.smarthome.model;

import java.io.Serializable;

/**
 * Created by fyc on 2017/8/1.
 */

public class Device implements Serializable {

    private String deviceName;
    private String deviceId;
    private int deviceStatus;// 设备是否在线；0、1
    private int deviceType;
    private int flag;  //标志，确定头部的几种类型 1、2、3
    private boolean isChoose;  //该设备是否已被场景选中

    public Device(String deviceName, String deviceId, int deviceStatus, int deviceType) {
        this.deviceName = deviceName;
        this.deviceId = deviceId;
        this.deviceStatus = deviceStatus;
        this.deviceType = deviceType;
    }

    public Device(int flag) {
        this.flag = flag;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
