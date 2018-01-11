package com.challengesstore.ui.login;

import com.arellomobile.mvp.MvpView;
import com.challengesstore.data.model.registration.UserSignIn;

/**
 * Created by felix on 1/9/18
 */

public interface LoginView extends MvpView {

    void onResponseError(String errorText);

    void onResponseSuccess(String response);

    void signIn();

    void showValidFieldError(UserSignIn userData);

    void setButtonEnabled(boolean isEnabled);
}
