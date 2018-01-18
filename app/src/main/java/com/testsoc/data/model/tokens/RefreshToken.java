package com.testsoc.data.model.tokens;

import com.google.gson.annotations.SerializedName;

/**
 * Created by felix on 1/17/18
 */

public class RefreshToken {

    @SerializedName("id")
    private long idUser;

    @SerializedName("refresh_token")
    private String refreshToken;

    public RefreshToken(long idUser, String refreshToken) {
        this.idUser = idUser;
        this.refreshToken = refreshToken;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
