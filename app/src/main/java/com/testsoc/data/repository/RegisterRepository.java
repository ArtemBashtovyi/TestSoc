package com.testsoc.data.repository;

import com.testsoc.data.api.ApiFactory;
import com.testsoc.data.model.register.UserSignIn;
import com.testsoc.data.model.register.UserSignUp;
import com.testsoc.data.model.tokens.AuthResponse;

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
