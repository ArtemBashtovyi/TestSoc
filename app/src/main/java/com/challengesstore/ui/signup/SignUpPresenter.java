package com.challengesstore.ui.signup;

import com.challengesstore.data.RegisterRepository;
import com.challengesstore.data.model.registration.UserSignUp;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by felix on 1/7/18
 */

public class SignUpPresenter {

    private SignUpView view;
    private RegisterRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();


    public SignUpPresenter(SignUpView view, RegisterRepository registerRepository) {
        this.view = view;
        this.repository = registerRepository;
    }


    public void onClick() {
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
    public void sendUserData(UserSignUp user) {

        final String json = new Gson().toJson(user);

        disposable.clear();
        disposable.add(repository
                .signUp(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (!response.isSuccessful()) {

                        view.onResponseError(response.errorBody().string() );

                        return;
                    }
                    view.onResponseSuccess();
                        } ,e -> view.onResponseError("Error internet connection")
                )
        );
    }

    // logic to verify is user input data valid
    public boolean isUserDataValid(UserSignUp user) {
        boolean isValid = true;
        String name = null;
        String surname = null;
        String password = null;
        String passwordRepeat = null;
        String email = null;

        if (user.getName().isEmpty() || user.getName().length() < 1) {
            name = "at least 1 character";
            isValid = false;
        }

        if (user.getSurname().isEmpty() || user.getSurname().length() < 1) {
            surname = "at least 1 character";
            isValid = false;
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()
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
        view.showValidFieldError(new UserSignUp(name,surname,email,password,passwordRepeat));
        return isValid;
    }


    void setError(int code){
        if (code == 401) {
            // email has used already
        }
        // TODO : HANDLE code and SHOW CERTAIN ERROR FOR EDIT TEXT WHEN RESPONSE OF NOT VALID DATA -> FROM SERVER
    }

    void onDestroy() {
        disposable.dispose();
        view = null;
    }

}
