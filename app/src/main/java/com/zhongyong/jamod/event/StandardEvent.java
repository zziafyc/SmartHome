package com.zhongyong.jamod.event;

import java.io.Serializable;

/**
 * Created by fyc on 2018/3/26.
 */

public class StandardEvent implements Serializable {
    String name;
    String value;

    public StandardEvent(String name, String value) {
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
