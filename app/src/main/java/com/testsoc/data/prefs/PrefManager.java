package com.testsoc.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by felix on 1/15/18
 */

public class PrefManager implements PrefContract {

    private static final String AUTH_DATA = "auth_data";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_ID_USER = "id_user";

    // FIXME : rewrite singleton
    private static PrefManager INSTANCE;

    private Context context;

    public static PrefManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PrefManager(context);
        }
        return INSTANCE;
    }
    private  PrefManager(Context context) {
        this.context = context;
    }

    private SharedPreferences getSharedPreferences(String prefsName) {
        return context.getSharedPreferences(prefsName,Context.MODE_PRIVATE);
    }

    @Override
    public void setAccessToken(@NonNull String accessToken) {
        getSharedPreferences(AUTH_DATA).edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Override
    public void setRefreshToken(@NonNull String refreshToken) {
        getSharedPreferences(AUTH_DATA).edit().putString(KEY_REFRESH_TOKEN,refreshToken).apply();
    }

    @Override
    public void setIdUser(long id) {
        getSharedPreferences(AUTH_DATA).edit().putLong(KEY_ID_USER,id).apply();
    }

    @Override
    public long getIdUser() {
        return getSharedPreferences(AUTH_DATA).getLong(KEY_ID_USER,1);
    }

    @Override
    public String getAccessToken() {
        return getSharedPreferences(AUTH_DATA).getString(KEY_ACCESS_TOKEN,"");
    }

    @Override
    public String getRefreshToken() {
        return getSharedPreferences(AUTH_DATA).getString(KEY_REFRESH_TOKEN,"");
    }


}
