package com.challengesstore.data.api.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by felix on 1/11/18
 */

public class UserTokenInterceptor implements Interceptor {

    private volatile OkHttpClient okHttpClient;

    public UserTokenInterceptor(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request newRequest  = chain.request();

        Request.Builder builder = newRequest.newBuilder();





        return chain.proceed(newRequest);

    }

    private void addAccessToken(Request.Builder builder) {

        builder.addHeader("Authorization", "Bearer ");
    }
}
