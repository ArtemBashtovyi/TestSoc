package com.challengesstore.data.api.service;

import com.challengesstore.data.model.register.UserSignIn;
import com.challengesstore.data.model.register.UserSignUp;
import com.challengesstore.data.model.tokens.AccessToken;
import com.challengesstore.data.model.tokens.AuthResponse;
import com.challengesstore.data.model.tokens.RefreshToken;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *  Service for registration and sign in
 */

public interface RegisterService {

    @POST("user/sign_up")
    Observable<Response<ResponseBody>> create(@Body UserSignUp userSignUp);

    @POST("user/login")
    Observable<Response<AuthResponse>> login(@Body UserSignIn userData);

    @POST("user/refresh_token")
    Call<AccessToken> updateAccessToken(@Body RefreshToken refreshToken);


}
