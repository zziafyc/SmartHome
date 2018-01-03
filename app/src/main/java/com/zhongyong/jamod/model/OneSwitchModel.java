package com.zhongyong.jamod.model;

import java.io.Serializable;

/**
 * Created by fyc on 2018/1/3.
 */

public class OneSwitchModel implements Serializable {
    private String id;
    private String name;
    private String state = "0";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
