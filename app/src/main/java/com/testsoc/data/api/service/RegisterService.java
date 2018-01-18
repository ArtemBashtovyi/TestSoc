package com.testsoc.data.api.service;

import com.testsoc.data.model.register.UserSignIn;
import com.testsoc.data.model.register.UserSignUp;
import com.testsoc.data.model.tokens.AccessToken;
import com.testsoc.data.model.tokens.AuthResponse;
import com.testsoc.data.model.tokens.RefreshToken;

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
