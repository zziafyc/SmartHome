package com.zhongyong.jamod.model;

import java.io.Serializable;

/**
 * Created by fyc on 2018/1/3.
 */

public class ZigBeeGateWayModel implements Serializable {
    private String ip;
    private String name;
    private String SNId;
    private String number;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSNId() {
        return SNId;
    }

    public void setSNId(String SNId) {
        this.SNId = SNId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
