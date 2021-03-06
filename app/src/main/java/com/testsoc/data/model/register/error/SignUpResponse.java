package com.testsoc.data.model.register.error;

import com.testsoc.data.model.register.UserSignUp;
import com.google.gson.annotations.SerializedName;

/**
 * Created by felix on 1/14/18
 */

public class SignUpResponse {

    @SerializedName("errors")
    private UserSignUp userSignUp;

    public SignUpResponse(UserSignUp userSignUp) {
        this.userSignUp = userSignUp;

    }

    public UserSignUp getUserSignUp() {
        return userSignUp;
    }

    public void setUserSignUp(UserSignUp userSignUp) {
        this.userSignUp = userSignUp;
    }
}
