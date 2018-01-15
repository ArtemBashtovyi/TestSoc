package com.challengesstore.ui.login;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.challengesstore.data.model.register.UserSignIn;

/**
 * Created by felix on 1/9/18
 */

public interface LoginView extends MvpView {

    void onResponseError(@NonNull String errorText);

    void onResponseSuccess();

    void signIn();

    void showValidFieldError(UserSignIn userData);

    void setButtonEnabled(boolean isEnabled);
}
