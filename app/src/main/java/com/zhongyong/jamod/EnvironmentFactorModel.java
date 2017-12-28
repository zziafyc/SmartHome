package com.zhongyong.jamod;

import java.io.Serializable;

/**
 * Created by fyc on 2017/12/28.
 */

public class EnvironmentFactorModel implements Serializable {
    private String name;
    private String value;

    public EnvironmentFactorModel(String name, String value) {
        this.name = name;
        this.value = value;
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
}
