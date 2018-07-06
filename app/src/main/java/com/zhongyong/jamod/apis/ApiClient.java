package com.zhongyong.jamod.apis;


import com.zhongyong.speechawake.Constants;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tiangongyipin on 16/2/21.
 */
public class ApiClient {

    private static final int DEFAULT_TIMEOUT = 30;

    private static Apis apis;

    private ApiClient() {
    }

    public static Apis getApis() {
        if (apis == null) {
            synchronized (ApiClient.class) {
                if (apis == null) {
                    OkHttpClient httpClient = new OkHttpClient.Builder()
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES)
                            //处理多BaseUrl,添加应用拦截器
                            .addInterceptor(new MoreBaseUrlInterceptor())
                            .build();
                    Retrofit restAdapter = new Retrofit.Builder()
                            .client(httpClient)
                            .baseUrl(Constants.API_URL)
                            //增加返回值为Gson的支持(以实体类返回)
                            .addConverterFactory(GsonConverterFactory.create())
                            //增加返回值为Oservable<T>的支持
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                    apis = restAdapter.create(Apis.class);
                }
            }
        }
        return apis;
    }

    //针对对个不同的baseUrl，自定义拦截器
    static class MoreBaseUrlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //获取原始的originalRequest
            Request originalRequest = chain.request();
            //获取老的url
            HttpUrl oldUrl = originalRequest.url();
            //获取originalRequest的创建者builder
            Request.Builder builder = originalRequest.newBuilder();
            //获取头信息的集合
            List<String> headerNames = originalRequest.headers("header_name");
            if (headerNames != null && headerNames.size() > 0) {
                //删除原有配置中的值,就是namesAndValues集合里的值
                builder.removeHeader("header_name");
                //获取头信息中配置的value,如：manage或者mdffx
                String headerName = headerNames.get(0);
                HttpUrl baseURL = null;
                //根据头信息中配置的value,来匹配新的base_url地址
                if ("repairServer".equals(headerName)) {
                    baseURL = HttpUrl.parse(Constants.API_REPAIR_URL);
                }
                //重建新的HttpUrl，需要重新设置的url部分
                HttpUrl newHttpUrl = oldUrl.newBuilder()
                        .scheme(baseURL.scheme())//http协议如：http或者https
                        .host(baseURL.host())//主机地址
                        .port(baseURL.port())//端口
                        .build();
                //获取处理后的新newRequest
                Request newRequest = builder.url(newHttpUrl).build();
                return chain.proceed(newRequest);
            } else {
                return chain.proceed(originalRequest);
            }

        }
    }

    public static Subscription call(Observable observable, MySubscriber subscriber) {
        return observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
