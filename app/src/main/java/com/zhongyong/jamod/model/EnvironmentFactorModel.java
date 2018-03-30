package com.zhongyong.jamod.model;

import java.io.Serializable;

/**
 * Created by fyc on 2017/12/28.
 */

public class EnvironmentFactorModel implements Serializable {
    private String name;
    private String value;
    private String standard;
    private boolean isStandard;

    public EnvironmentFactorModel(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public EnvironmentFactorModel(String name, String value, String standard, boolean isStandard) {
        this.name = name;
        this.value = value;
        this.standard = standard;
        this.isStandard = isStandard;
    }

    public boolean isStandard() {
        return isStandard;
    }

    public void setStandard(boolean standard) {
        isStandard = standard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }
}
