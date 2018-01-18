package com.challengesstore.data.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.challengesstore.BuildConfig;
import com.challengesstore.data.api.interceptor.UserTokenInterceptor;
import com.challengesstore.data.api.service.RegisterService;
import com.challengesstore.data.api.service.UserService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiFactory {

    private final static String TAG_TOKEN = "ApiFactoryToken";

    private volatile static RegisterService registerService;
    private volatile static UserService userService;

    private static OkHttpClient okHttpClient;


    public static UserService getUserService(@NonNull Context context) {
        if (userService == null) {
            synchronized (ApiFactory.class) {
                if (userService == null) {
                    userService = createServiceBuilder()
                            .client(createUserService(context))
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
                .baseUrl(BuildConfig.API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    @NonNull
    private static OkHttpClient createUserService(Context context) {
        return  createBuilder()
                .addInterceptor(new UserTokenInterceptor(context))
                .addInterceptor(createLogInterceptor())
                .build();

    }



    // Двойная проверка
    private static OkHttpClient createRegisterClient() {
        if (okHttpClient == null) {
            synchronized (ApiFactory.class) {
                if (okHttpClient == null) {
                    okHttpClient = createBuilder()
                            .addInterceptor(chain -> {
                                Request request = chain.request();

                                Request.Builder builder = request.newBuilder();
                                builder.addHeader("Content-Type","application/json");

                                Request response = builder.build();
                                return chain.proceed(response);
                            })
                            .addInterceptor(createLogInterceptor())
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    @NonNull
    private static OkHttpClient.Builder createBuilder() {
        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15,TimeUnit.SECONDS);
    }


    private static HttpLoggingInterceptor createLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }





}

