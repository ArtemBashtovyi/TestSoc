package com.challengesstore.data.model.tokens;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by felix on 1/15/18
 */

public class AccessToken {

    @SerializedName("token")
    @NonNull
    private String accessToken;

    /*@SerializedName("token_type")
    private String tokenType;*/

    public AccessToken(@NonNull String accessToken,@NonNull String tokenType) {
        this.accessToken = accessToken;
        //this.tokenType = "SAs";
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(@NonNull String accessToken) {
        this.accessToken = accessToken;
    }

  /*  public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }*/
}
