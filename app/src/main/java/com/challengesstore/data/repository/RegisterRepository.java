package com.challengesstore.data.repository;

import com.challengesstore.data.api.ApiFactory;
import com.challengesstore.data.model.register.UserSignIn;
import com.challengesstore.data.model.register.UserSignUp;
import com.challengesstore.data.model.tokens.AuthResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by felix on 1/7/18
 */

public class RegisterRepository {

    public Observable<Response<ResponseBody>> signUp(UserSignUp userSignUp) {
        return ApiFactory.getRegisterService().create(userSignUp);
    }

    public Observable<Response<AuthResponse>> signIn(UserSignIn userData) {
        return ApiFactory.getRegisterService().login(userData);
    }

}
