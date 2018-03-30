package com.zhongyong.smarthome.api;

import java.io.Serializable;

/**
 * Created by fyc on 2017/11/16.
 */

public class HttpResult<T> implements Serializable {
    public int resultCode;
    public String message;
    public T data;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
