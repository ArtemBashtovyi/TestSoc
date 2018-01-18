package com.challengesstore.data.api.interceptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.challengesstore.data.model.tokens.AccessToken;
import com.challengesstore.data.model.tokens.RefreshToken;
import com.challengesstore.data.prefs.PrefManager;
import com.challengesstore.data.repository.AuthRepository;
import com.challengesstore.ui.login.LoginActivity;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

/**
 * Created by felix on 1/11/18
 */

public class UserTokenInterceptor implements Interceptor {

    // TODO : vars as response exceptions
    private static final int CODE_BAD_REQUEST_RESPONSE = 400;
    private static final int CODE_SUCCESS_RESPONSE = 202;

    private static final String TAG_TOKEN = "TokenInterceptor" ;


    private Context context;
    private ReentrantLock lock = new ReentrantLock();
    private AuthRepository repository;

    public UserTokenInterceptor(Context context) {
        this.context = context;
        PrefManager prefManager = PrefManager.getInstance(context);
        repository = new AuthRepository(prefManager);
    }


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request request = chain.request();

        String accessToken = repository.getAccessToken();

        Request.Builder requestBuilder = request.newBuilder();
        setAuthHeader(requestBuilder,accessToken);

        //requestBuilder.addHeader("Authorization", "Bearer " + accessToken);

        Log.i(TAG_TOKEN,"FirstTime " + accessToken);

        request = requestBuilder.build();
        Response response = chain.proceed(request);

        if (response.code() == CODE_BAD_REQUEST_RESPONSE) {
            lock.lock();
            try {
                String updatedAccessToken = repository.getAccessToken();

                if (updatedAccessToken != null
                        && updatedAccessToken.equals(accessToken))  {

                    // handle error updating access_token
                    // and exceptions if updating access_token fail
                    int codeTokenRefresh = updateAccessToken();
                    if (codeTokenRefresh != CODE_SUCCESS_RESPONSE) {
                        if (codeTokenRefresh == CODE_BAD_REQUEST_RESPONSE) logout();
                    }
                }

                // get exactly new token for all threads
                // thread which execute refreshToken action will receive new token too
                updatedAccessToken = repository.getAccessToken();

                Log.i(TAG_TOKEN,"UpdatedTokenPutting " + updatedAccessToken);

                Request requestNew = chain.request();
                requestBuilder = requestNew.newBuilder();
                setAuthHeader(requestBuilder,updatedAccessToken);

                /*requestNew = requestNew.newBuilder()
                        .header("Authorization", "Bearer "
                                + updatedAccessToken).build();*/

                // retry appropriate request
                response = chain.proceed(requestNew);
                return response;
            } finally {
                lock.unlock();
            }

        }

        return response;
    }


    private void setAuthHeader(Request.Builder builder, String token) {
        if (token != null)
            builder.header("Authorization", String.format("Bearer %s", token));
    }

    // do new call for update access_token
    // synchronized call because all threads do async calls
    private int updateAccessToken() {
        int result;

        // get previous refresh_token
        String refreshToken = repository.getRefreshToken();
        long idUser = repository.getId();

        try {
            Call<AccessToken> call =
                    repository.updateAccessToken(new RefreshToken(idUser,refreshToken));

            retrofit2.Response<AccessToken> response = call.execute();
            if (response.isSuccessful()) {

                Log.i("RefreshAccessTokenFunc", response.body().getAccessToken());
                repository.setAccessToken(response.body().getAccessToken());

                result = response.code();

            } else result = response.code();

        } catch (IOException e) {
            e.printStackTrace();
            result = CODE_BAD_REQUEST_RESPONSE;
        }

        return result;
    }


    // TODO : logout impl
    private void logout() {
        LoginActivity.start(context);
    }

}
