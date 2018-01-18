package com.testsoc.data.api.service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 *  Control all user data
 */

public interface UserService {

    @Multipart
    @POST("user/update_profile")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image,
                                 @Part("firstName") RequestBody name,
                                 @Part("lastName") RequestBody secondName);

    @GET("welcome")
    Call<ResponseBody> welcome();

    @GET("user/get_user_info/{id}")
    Call<ResponseBody> getUser(@Path("id") long id);


}
