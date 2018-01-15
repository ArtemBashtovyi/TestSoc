package com.challengesstore.ui.signup;

import android.support.annotation.Nullable;
import android.util.Log;

import com.challengesstore.data.model.register.UserSignUp;
import com.challengesstore.data.model.register.error.DataValidationError;
import com.challengesstore.data.repository.RegisterRepository;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by felix on 1/7/18
 */

public class SignUpPresenter {

    private static final int ERROR_BAD_REQUEST_CODE = 400;

    private SignUpView view;
    private RegisterRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();


    public SignUpPresenter(SignUpView view, RegisterRepository registerRepository) {
        this.view = view;
        this.repository = registerRepository;
    }

    void onClick() {
        view.signUp();
    }

    // main method which start sequence
    public void signUp(UserSignUp userData) {

        // check data validation
        if (!isUserDataValid(userData)) {
            view.setButtonEnabled(true);
            return;
        }

        // if data valid send data to server
        view.setButtonEnabled(false);
        sendUserData(userData);
    }


    // send POST to server

    private void sendUserData(UserSignUp user) {
        final String json = new Gson().toJson(user);

        disposable.clear();

        disposable.add(repository
                .signUp(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (!response.isSuccessful()) {

                        onResponseError(response.code(),response.errorBody().string());
                        return;
                    }
                    view.onResponseSuccess();
                    Log.i("Response ", String.valueOf(response.body().string()));
                        } ,e -> view.onResponseError("Error internet connection")
                )
        );
    }

    // logic to verify is user input data valid
    private boolean isUserDataValid(UserSignUp user) {

        boolean isValid = true;
        String name = null;
        String surname = null;
        String userName = null;
        String password = null;
        String passwordRepeat = null;
        String email = null;

        if (user.getName().isEmpty() || user.getName().length() < 1) {
            name = "at least 1 character";
            isValid = false;
        }

        if (user.getUserName().isEmpty() || user.getUserName().length() < 1) {
            userName = "this user name already exists";
            isValid = false;
        }

        if (user.getSurname().isEmpty() || user.getSurname().length() < 1) {
            surname = "at least 1 character";
            isValid = false;
        }

        if (user.getEmail().isEmpty() || user.getEmail().isEmpty()
                /*|| !android.util.Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches()*/) {
            email = "enter a valid email address";
            isValid = false;
        }

        if (user.getPassword().isEmpty() || user.getPassword().length() < 3 || user.getPassword().length() > 30) {
            password = "between 8 and 30 characters";
            isValid = false;
        }

        if (!user.getPasswordRepeat().equals(user.getPassword())) {
            passwordRepeat = "passwords doesn't match";
            isValid = false;
        }

        // if all clauses have verified then send null,null...
        view.showValidFieldError(new UserSignUp(name,surname,userName,email,password,passwordRepeat));
        return isValid;
    }


     private void onResponseError(int code,@Nullable String errorJson){
        if (errorJson != null) {
            if (code == ERROR_BAD_REQUEST_CODE) {
                try {
                    UserSignUp invalidFields = new Gson()
                            .fromJson(errorJson, DataValidationError.class)
                            .getUserSignUp();

                    view.showValidFieldError(invalidFields);

                } catch (Exception e) {
                    view.onResponseError("Input data invalid");
                }

            } else {
                view.onResponseError("Unknown error");
            }
        } else view.onResponseError("Error");
    }

    void onDestroy() {
        disposable.dispose();
        view = null;
    }

}
