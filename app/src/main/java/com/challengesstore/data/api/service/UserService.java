package com.challengesstore.data.api.service;

import com.challengesstore.data.model.register.response.AccessToken;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 *  Control all user data
 */

public interface UserService {

    @Multipart
    @POST("user/update_profile")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image,
                                 @Part("firstName") RequestBody name,
                                 @Part("lastName") RequestBody secondName);

    @POST("user/refresh_token")
    Call<AccessToken> updateAccessToken(@Body String refreshToken,@Body int id);


    @GET("welcome")
    Call<ResponseBody> welcome();


}
