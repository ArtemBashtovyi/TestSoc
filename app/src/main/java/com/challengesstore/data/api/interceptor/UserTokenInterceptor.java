package com.challengesstore.data.api.interceptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.challengesstore.data.api.ApiFactory;
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

    private static final String TAG_TOKEN = "Interceptor" ;
    private Context context;
    private ReentrantLock reentrantLock = new ReentrantLock();
    
    
    public UserTokenInterceptor(Context context) {
        this.context = context;
    }


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request request = chain.request();

        // caching logic
        PrefManager prefManager = PrefManager.getInstance(context);
        AuthRepository authRepository = new AuthRepository(prefManager);

        String accessToken = authRepository.getAccessToken();

        Request.Builder requestBuilder = request.newBuilder();
        requestBuilder.addHeader("Authorization", "Bearer " + accessToken);

        Log.i(TAG_TOKEN,"FirstTime " + accessToken);

        request = requestBuilder.build();

        Response response = chain.proceed(request);

        if (response.code() == 400) {
            // double-locking check tokens

            String updatedAccessToken = authRepository.getAccessToken();

            synchronized (ApiFactory.class) {
                int codeTokenRefresh = refreshAccessToken();
                updatedAccessToken = authRepository.getAccessToken();

                if (updatedAccessToken != null
                        && !updatedAccessToken.equals(accessToken))  {

                    // 401 - error code
                    if (codeTokenRefresh != 401) {

                        if (codeTokenRefresh == 400) logout();
                        Log.i(TAG_TOKEN,"UpdatedTokenPutting " + updatedAccessToken);
                        // if codeTokenRefresh not error
                        // make query with new Token
                        Request requestNew = chain.request();
                        requestNew = requestNew.newBuilder()
                                .header("Authorization", "Bearer "
                                        + updatedAccessToken).build();
                        response = chain.proceed(requestNew);
                        return response;
                    }
                }
            }
        }

        return response;
    }


    private int refreshAccessToken() {
        int result;
        // FIXME : THIS SHIT
        PrefManager prefManager = PrefManager.getInstance(context);
        AuthRepository authRepository = new AuthRepository(prefManager);

        String refreshToken = authRepository.getRefreshToken();
        long idUser = authRepository.getId();

        try {
            Call<AccessToken> call =
                    authRepository.updateAccessToken(new RefreshToken(idUser,refreshToken));

            retrofit2.Response<AccessToken> response = call.execute();
            if (response.isSuccessful()) {
                Log.i("RefreshAccessTokenFunc", response.body().getAccessToken());
                prefManager.setAccessToken(response.body().getAccessToken());

                result = response.code();
            } else result = response.code();

        } catch (IOException e) {
            e.printStackTrace();
            result = 400;
        }

        return result;
    }


    private void logout() {
        LoginActivity.start(context);
    }

}
