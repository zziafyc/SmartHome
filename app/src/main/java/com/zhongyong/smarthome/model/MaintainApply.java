package com.zhongyong.smarthome.model;

import java.io.Serializable;

public class MaintainApply implements Serializable {
    //请求时间
    private String genTime;
    //设备序列号
    private String serialNum;

    public String getGenTime() {
        return genTime == null ? "" : genTime;
    }

    public void setGenTime(String genTime) {
        this.genTime = genTime;
    }

    public String getSerialNum() {
        return serialNum == null ? "" : serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }
}
