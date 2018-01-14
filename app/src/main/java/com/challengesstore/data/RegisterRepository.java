package com.challengesstore.data;

import com.challengesstore.data.api.register.ApiFactory;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by felix on 1/7/18
 */

public class RegisterRepository {

    public Observable<Response<ResponseBody>> signUp(String json) {
        return ApiFactory.getRegisterService().create(json);
    }

    public Observable<Response<ResponseBody>> signIn(String userData) {
        return ApiFactory.getRegisterService().login(userData);
    }

    public Call<ResponseBody> welcome() {
        return ApiFactory.getUserService().welcome();
    }
}
