package com.challengesstore.data.api.register;

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
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiFactory {

    private static RegisterService registerService;
    private static UserService userService;

    private static OkHttpClient registerClient;


    public static UserService getUserService() {
        if (userService == null) {
            synchronized (ApiFactory.class) {
                if (userService == null) {
                    userService = createServiceBuilder()
                            .client(createUserClient())
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
    private static Retrofit.Builder createServiceBuilder() {
        return new Retrofit.Builder()
                /*.client(buildClient())*/
                .baseUrl(BuildConfig.API_ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }




    private static OkHttpClient createUserClient() {
        if (registerClient == null) {
            synchronized (ApiFactory.class) {
                if (registerClient == null) {
                    registerClient = createBuilder()
                            .addInterceptor(new UserTokenInterceptor())
                            .build();
                }
            }
        }
        return registerClient;
    }

    // Двойная проверка
    private static OkHttpClient createRegisterClient() {
        if (registerClient == null) {
            synchronized (ApiFactory.class) {
                if (registerClient == null) {
                    registerClient = createBuilder().build();
                }
            }
        }
        return registerClient;
    }

    @NonNull
    private static OkHttpClient.Builder createBuilder() {
        return new OkHttpClient.Builder()
                .addInterceptor(createLogInterceptor())
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

