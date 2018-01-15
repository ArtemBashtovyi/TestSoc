package com.challengesstore.data.model.register.error;

import com.challengesstore.data.model.register.UserSignUp;
import com.google.gson.annotations.SerializedName;

/**
 * Created by felix on 1/14/18
 */

public class DataValidationError {

    @SerializedName("errors")
    private UserSignUp userSignUp;

    public DataValidationError(UserSignUp userSignUp) {
        this.userSignUp = userSignUp;
    }

    public UserSignUp getUserSignUp() {
        return userSignUp;
    }

    public void setUserSignUp(UserSignUp userSignUp) {
        this.userSignUp = userSignUp;
    }
}
