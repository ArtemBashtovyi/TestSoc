package com.challengesstore.data.api.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *  Service for registration and sign in
 */

public interface RegisterService {

    @POST("user/signUp")
    Observable<Response<ResponseBody>> create(@Body String json);

    @POST("user/signIn")
    Observable<Response<ResponseBody>> login(@Body String userData);


}