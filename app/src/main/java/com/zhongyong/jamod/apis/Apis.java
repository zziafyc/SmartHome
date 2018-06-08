package com.zhongyong.jamod.apis;


import com.zhongyong.jamod.model.ModBusGateWayModel;
import com.zhongyong.jamod.model.User;
import com.zhongyong.smarthome.api.HttpResult;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by fyc on 2017/3/6
 * 邮箱：847891359@qq.com
 * 博客：http://blog.csdn.net/u013769274
 */

public interface Apis {
    //用户注册
    @POST("user/register")
    Observable<HttpResult<User>> register(@Body User user);

    //用户登录
    @POST("user/login")
    Observable<HttpResult<User>> login(@Body User user);

    //拉取modbus设备数据
    @POST("modBusGateWay/getModBusList")
    Observable<HttpResult<List<ModBusGateWayModel>>> getModBusList(@Body ModBusGateWayModel modBusGateWayModel);

    //创建一个modbus设备的信息
    @POST("modBusGateWay/addModBus")
    Observable<HttpResult<Void>> addModBus(@Body ModBusGateWayModel modBusGateWayModel);

    //删除一个modbus设备的信息
    @POST("modBusGateWay/deleteModBus")
    Observable<HttpResult<Void>> deleteModBus(@Query("id") int id);

    //这个登录
    @GET("authWeb4/UserInfo/")
    String loginToNiagara(@Header("authorization") String auth);


}



