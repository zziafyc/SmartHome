package com.zhongyong.smarthome.model;

import java.io.Serializable;

/**
 * Created by fyc on 2017/7/31.
 */

public class Member implements Serializable {
    String picUrl;
    String nickName;
    String id;
    int isOpen;   //为0就未开启了权限，为1就开启了权限

    public Member(String picUrl, String nickName, String id, int isOpen) {
        this.picUrl = picUrl;
        this.nickName = nickName;
        this.id = id;
        this.isOpen = isOpen;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }
}

