package com.challengesstore.data.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.challengesstore.BuildConfig;
import com.challengesstore.data.api.interceptor.UserTokenInterceptor;
import com.challengesstore.data.api.service.RegisterService;
import com.challengesstore.data.api.service.UserService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiFactory {

    private volatile static RegisterService registerService;
    private volatile static UserService userService;


    public static UserService getUserService(@NonNull Context context) {
        if (userService == null) {
            synchronized (ApiFactory.class) {
                if (userService == null) {
                    userService = createServiceBuilder()
                            .client(createUserServiceClient(context))
                            .build()
                            .create(UserService.class);
                }
            }
        }
        return userService;
    }


    public static RegisterService getRegisterService() {
        if (registerService == null) {
            synchronized (ApiFactory.class) {
                if (registerService == null) {
                    registerService = createServiceBuilder()
                            .client(createRegisterClient())
                            .build()
                            .create(RegisterService.class);
                }
            }
        }
        return registerService;
    }


    @NonNull
    private static OkHttpClient createUserServiceClient(Context context) {
        return  createOkHttpBuilder()
                .addInterceptor(new UserTokenInterceptor(context))
                .build();
    }


    @NonNull
    private static OkHttpClient createRegisterClient() {
        return createOkHttpBuilder()
                .build();
    }


    @NonNull
    private static Retrofit.Builder createServiceBuilder() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    @NonNull
    private static OkHttpClient.Builder createOkHttpBuilder() {
        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15,TimeUnit.SECONDS)
                .addInterceptor(createLogInterceptor());
    }


    private static HttpLoggingInterceptor createLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

}

