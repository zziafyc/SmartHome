package com.zhongyong.jamod.model;

import com.fbee.zllctl.DeviceInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fyc on 2018/1/3.
 */

public class ZigBeeSceneModel implements Serializable {
    private String name;
    private String englishName;
    private String zigBeeGatewayIp;
    private OneSwitchModel mOneSwitchModel = new OneSwitchModel();
    private TwoSwitchModel mTwoSwitchModel = new TwoSwitchModel();
    private ThreeSwitchModel mThreeSwitchModel = new ThreeSwitchModel();
    //这个是飞比的设备信息
    private List<DeviceInfo> mDeviceInfos;
    //这个是modbus网关的设备信息
    private List<ModBusGateWayModel> mModBusGateWayModels;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getZigBeeGatewayIp() {
        return zigBeeGatewayIp;
    }

    public void setZigBeeGatewayIp(String zigBeeGatewayIp) {
        this.zigBeeGatewayIp = zigBeeGatewayIp;
    }

    public TwoSwitchModel getTwoSwitchModel() {
        return mTwoSwitchModel;
    }

    public void setTwoSwitchModel(TwoSwitchModel twoSwitchModel) {
        mTwoSwitchModel = twoSwitchModel;
    }

    public ThreeSwitchModel getThreeSwitchModel() {
        return mThreeSwitchModel;
    }

    public void setThreeSwitchModel(ThreeSwitchModel threeSwitchModel) {
        mThreeSwitchModel = threeSwitchModel;
    }

    public List<DeviceInfo> getDeviceInfos() {
        return mDeviceInfos;
    }

    public void setDeviceInfos(List<DeviceInfo> deviceInfos) {
        mDeviceInfos = deviceInfos;
    }

    public List<ModBusGateWayModel> getModBusGateWayModels() {
        return mModBusGateWayModels;
    }

    public void setModBusGateWayModels(List<ModBusGateWayModel> modBusGateWayModels) {
        mModBusGateWayModels = modBusGateWayModels;
    }

    public OneSwitchModel getOneSwitchModel() {
        return mOneSwitchModel;
    }

    public void setOneSwitchModel(OneSwitchModel oneSwitchModel) {
        mOneSwitchModel = oneSwitchModel;
    }
}
