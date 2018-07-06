package com.zhongyong.smarthome.model;

import java.io.Serializable;

public class RepairApply implements Serializable {
    private String serialNum;
    private String problem;
    private String applyTime;
    private String repairNum;
    private int urgency;

    public String getSerialNum() {
        return serialNum == null ? "" : serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getProblem() {
        return problem == null ? "" : problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getApplyTime() {
        return applyTime == null ? "" : applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getRepairNum() {
        return repairNum == null ? "" : repairNum;
    }

    public void setRepairNum(String repairNum) {
        this.repairNum = repairNum;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }
}
