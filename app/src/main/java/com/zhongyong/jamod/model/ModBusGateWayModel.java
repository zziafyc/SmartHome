package com.zhongyong.jamod.model;

import java.io.Serializable;

/**
 * Created by fyc on 2017/12/28.
 */

public class ModBusGateWayModel implements Serializable {
    private int id;
    private String userId;
    private String sceneId;
    private String name;
    private String ip;
    private int unitId;
    private boolean flag;  //true表示服务端数据，false表示本地数据

    public ModBusGateWayModel() {
    }

    public ModBusGateWayModel(String name, String ip, int unitId) {
        this.name = name;
        this.ip = ip;
        this.unitId = unitId;
    }

    public ModBusGateWayModel(int id, String userId, String sceneId, String name, String ip, int unitId) {
        this.id = id;
        this.userId = userId;
        this.sceneId = sceneId;
        this.name = name;
        this.ip = ip;
        this.unitId = unitId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
