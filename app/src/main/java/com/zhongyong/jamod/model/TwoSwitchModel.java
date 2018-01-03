package com.zhongyong.jamod.model;

import java.io.Serializable;

/**
 * Created by fyc on 2018/1/3.
 */

public class TwoSwitchModel implements Serializable {
    private String leftId;
    private String leftName;
    private String leftState = "0";
    private String rightId;
    private String rightName;
    private String rightState = "0";

    public String getLeftId() {
        return leftId;
    }

    public void setLeftId(String leftId) {
        this.leftId = leftId;
    }

    public String getLeftName() {
        return leftName;
    }

    public void setLeftName(String leftName) {
        this.leftName = leftName;
    }

    public String getLeftState() {
        return leftState;
    }

    public void setLeftState(String leftState) {
        this.leftState = leftState;
    }

    public String getRightId() {
        return rightId;
    }

    public void setRightId(String rightId) {
        this.rightId = rightId;
    }

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public String getRightState() {
        return rightState;
    }

    public void setRightState(String rightState) {
        this.rightState = rightState;
    }
}
