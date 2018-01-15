package com.challengesstore.ui.signup;

import com.challengesstore.data.model.register.UserSignUp;

/**
 * Created by felix on 1/7/18
 */

public interface SignUpView {

    void onResponseError(String errorText);

    void onResponseSuccess();

    void signUp();

    void showValidFieldError(UserSignUp user);

    void setButtonEnabled(boolean isEnabled);
}
