package com.challengesstore.data.api.register;

import android.support.annotation.NonNull;

import com.challengesstore.BuildConfig;
import com.challengesstore.data.api.service.RegisterService;
import com.challengesstore.data.api.service.UserService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiFactory {

    private static RegisterService registerService;
    private static UserService userService;

    private static OkHttpClient okHttpClient;


    public static UserService getUserService() {
        if (userService == null) {
            synchronized (ApiFactory.class) {
                if (userService == null) {
                    userService = createService().create(UserService.class);
                }
            }
        }
        return userService;
    }


    public static RegisterService getRegisterService() {
        if (registerService == null) {
            synchronized (ApiFactory.class) {
                if (registerService == null) {
                    registerService = createService().create(RegisterService.class);
                }
            }
        }
        return registerService;
    }


    @NonNull
    private static Retrofit createService() {
        return new Retrofit.Builder()
                .client(buildClient())
                .baseUrl(BuildConfig.API_ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }



    // Двойная проверка
    private static OkHttpClient buildClient() {
        if (okHttpClient == null) {
            synchronized (ApiFactory.class) {
                if (okHttpClient == null) {
                    okHttpClient = createClient();
                }
            }
        }
        return okHttpClient;
    }

    @NonNull
    private static OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15,TimeUnit.SECONDS)
                .addInterceptor(createLogInterceptor())
                .build();
    }


    private static HttpLoggingInterceptor createLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}

