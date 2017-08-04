package com.zhongyong.smarthome.model;

/**
 * Created by fyc on 2017/8/2.
 */

public class Scene {
    private int position;
    private String url;
    private String name;
    private int status;  //0:表示关闭状态，1：开启状态

    public Scene(int position, String url, String name,int status) {
        this.position = position;
        this.url = url;
        this.name = name;
        this.status=status;
    }

    public int getPosition() {
        return position;
    }

    public Scene setPosition(int position) {
        this.position = position;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Scene setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getName() {
        return name;
    }

    public Scene setName(String name) {
        this.name = name;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}