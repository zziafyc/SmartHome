package com.zhongyong.smarthome.api;

/**
 * Created by fyc on 2017/12/8.
 */

public class ErrorResult {
    private int errorCode;
    private int logId;
    private String errorMessage;

    public ErrorResult(int errorCode, int logId, String errorMessage) {
        this.errorCode = errorCode;
        this.logId = logId;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
