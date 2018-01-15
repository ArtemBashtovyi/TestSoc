package com.challengesstore.data.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.challengesstore.BuildConfig;
import com.challengesstore.data.api.service.RegisterService;
import com.challengesstore.data.api.service.UserService;
import com.challengesstore.data.model.register.response.AccessToken;
import com.challengesstore.data.prefs.PrefManager;
import com.challengesstore.data.repository.AuthRepository;
import com.challengesstore.ui.login.LoginActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiFactory {

    private volatile static RegisterService registerService;
    private volatile static UserService userService;

    private static OkHttpClient okHttpClient;

    public static UserService getUserService(@NonNull Context context) {
        if (userService == null) {
            synchronized (ApiFactory.class) {
                if (userService == null) {
                    userService = createServiceBuilder()
                            .client(createUserClient(context))
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
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }


    private static OkHttpClient createUserClient(@NonNull Context context) {
        if (okHttpClient == null) {
            synchronized (ApiFactory.class) {
                if (okHttpClient == null) {
                    okHttpClient = createBuilder()
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                Request request = chain.request();

                                // caching logic
                                PrefManager prefManager = PrefManager.getInstance(context);
                                AuthRepository authRepository = new AuthRepository(prefManager);

                                String accessToken = authRepository.getAccessToken();

                                Request.Builder requestBuilder = request.newBuilder();
                                requestBuilder.addHeader("Authorization", "Bearer " + accessToken);

                                request = requestBuilder.build();
                                Response response = chain.proceed(request);

                                if (response.code() == 401) {
                                    synchronized (okHttpClient) {
                                        String updatedAccessToken = authRepository.getAccessToken();

                                        if (updatedAccessToken != null
                                                && updatedAccessToken.equals(accessToken))  {
                                            int codeTokenRefresh = refreshAccessToken();

                                                if (codeTokenRefresh != 202) {
                                                    if (codeTokenRefresh == 400) logout();
                                                    return response;
                                                }
                                        }
                                    }
                                }
                                String newAccessToken = authRepository.getAccessToken();
                                if (newAccessToken != null) {
                                    requestBuilder.header("Authorization", "Bearer " + newAccessToken);
                                    request = requestBuilder.build();
                                    return chain.proceed(request);
                                }

                                return response;
                            }
                                private int refreshAccessToken() {
                                    int result;
                                    // FIXME : THIS SHIT
                                    PrefManager prefManager = PrefManager.getInstance(context);
                                    AuthRepository authRepository = new AuthRepository(prefManager);

                                    String refreshToken = authRepository.getRefreshToken();

                                    try {

                                        Call<AccessToken> call =
                                                authRepository.updateAccessToken(refreshToken,context);

                                        retrofit2.Response<AccessToken> response = call.execute();
                                        if (response.isSuccessful()) {
                                            prefManager.setAccessToken(response.body().getAccessToken());

                                            result = response.code();
                                            Log.i("UpdatedNewAccessToken", response.body().getAccessToken());
                                        } else result = response.code();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        result = 400;
                                    }

                                    return result;
                                }


                                private void logout() {
                                    LoginActivity.start(context);
                                }

                            })

                            .build();
                }
            }
        }
        return okHttpClient;
    }


    // Двойная проверка
    private static OkHttpClient createRegisterClient() {
        if (okHttpClient == null) {
            synchronized (ApiFactory.class) {
                if (okHttpClient == null) {
                    okHttpClient = createBuilder().build();
                }
            }
        }
        return okHttpClient;
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

