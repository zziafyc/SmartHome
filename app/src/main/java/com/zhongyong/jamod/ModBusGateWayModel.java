package com.zhongyong.jamod;

import java.io.Serializable;

/**
 * Created by fyc on 2017/12/28.
 */

public class ModBusGateWayModel implements Serializable {
    private String name;
    private String ip;
    private int ref;
    private int count;
    private int unitId;

    public ModBusGateWayModel(String name,String ip, int unitId) {
        this.name=name;
        this.ip = ip;
        this.unitId = unitId;
    }

    public ModBusGateWayModel(String ip, int ref, int count, int unitId) {
        this.ip = ip;
        this.ref = ref;
        this.count = count;
        this.unitId = unitId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
