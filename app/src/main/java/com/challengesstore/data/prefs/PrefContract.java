package com.challengesstore.data.prefs;

import android.support.annotation.NonNull;

/**
 * Created by felix on 1/15/18
 */

public interface PrefContract {

    void setAccessToken(@NonNull String accessToken);
    void setRefreshToken(@NonNull String refreshToken);
    void setIdUser(long id);

    long getIdUser();
    String getAccessToken();
    String getRefreshToken();



}
