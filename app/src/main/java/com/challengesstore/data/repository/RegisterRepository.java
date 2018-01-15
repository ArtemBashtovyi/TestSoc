package com.challengesstore.data.repository;

import com.challengesstore.data.api.ApiFactory;
import com.challengesstore.data.model.register.response.AuthResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by felix on 1/7/18
 */

public class RegisterRepository {

    public Observable<Response<ResponseBody>> signUp(String json) {
        return ApiFactory.getRegisterService().create(json);
    }

    public Observable<Response<AuthResponse>> signIn(String userData) {
        return ApiFactory.getRegisterService().login(userData);
    }

}
