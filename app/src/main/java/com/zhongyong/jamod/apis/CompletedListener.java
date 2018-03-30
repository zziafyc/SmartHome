package com.zhongyong.jamod.apis;


import com.zhongyong.smarthome.api.ErrorResult;

/**
 * Created by fyc on 2017/12/8.
 */

public interface CompletedListener {

    void onCompleted(String result);

    void onError(ErrorResult errorResult);
}
