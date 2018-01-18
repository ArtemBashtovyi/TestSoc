package com.testsoc.data.model.tokens;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by felix on 1/15/18
 */

public class AccessToken {

    @SerializedName("token")
    @NonNull
    private String accessToken;

    public AccessToken(@NonNull String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(@NonNull String accessToken) {
        this.accessToken = accessToken;
    }

}
