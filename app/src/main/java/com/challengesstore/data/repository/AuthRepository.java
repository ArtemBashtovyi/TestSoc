package com.challengesstore.data.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.challengesstore.data.api.ApiFactory;
import com.challengesstore.data.model.register.response.AccessToken;
import com.challengesstore.data.prefs.PrefManager;

import retrofit2.Call;

/**
 * Created by felix on 1/15/18
 */

public class AuthRepository {

    private PrefManager prefManager;

    public AuthRepository(PrefManager prefManager) {
        this.prefManager = prefManager;
    }

    public String getAccessToken() {
        return prefManager.getAccessToken();
    }

    public String getRefreshToken() {
        return prefManager.getRefreshToken();
    }

    public void setAccessToken(@NonNull String accessToken) {
        prefManager.setAccessToken(accessToken);
    }

    public void setRefreshToken(@NonNull String refreshToken) {
        prefManager.setRefreshToken(refreshToken);
    }

    public Call<AccessToken> updateAccessToken(String refreshToken, Context context) {
        return ApiFactory.getUserService(context).updateAccessToken(refreshToken,19);
    }


}
