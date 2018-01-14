package com.challengesstore.ui.login;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.challengesstore.data.RegisterRepository;
import com.challengesstore.data.model.registration.UserSignIn;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginView> {

    private RegisterRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();

    public LoginPresenter() {
    }

    public LoginPresenter(RegisterRepository repository) {
        this.repository = repository;
    }

    void onButtonSignInClick(){
        getViewState().signIn();
    }

    public void signIn(final UserSignIn userData) {

        if (!isUserDataValid(userData)) {
            getViewState().setButtonEnabled(true);
            return;
        }

        sendUserData(userData);
        getViewState().setButtonEnabled(false);
    }

    public void sendUserData(UserSignIn user) {
        // clear previous observables if not first time click
        disposable.clear();

        final String json = new Gson().toJson(user);

        disposable.add(repository
                .signIn(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {
                    if (!responseBody.isSuccessful()) {
                        getViewState().onResponseError(responseBody.errorBody().string());
                        return;
                    }

                    // TODO : Check handling json with API USER KEY -> FROM SERVER
                    getViewState().onResponseSuccess(responseBody.body().string());
                    Log.i("ResponseFull", responseBody.message());

                }, e -> getViewState().onResponseError("Error internet connection")));
    }



    public void setError(int code) {
        // TODO : HANDLE ERROR CODE
    }

    public boolean isUserDataValid(UserSignIn userData) {
        boolean isValid = true;
        String email = null;
        String password = null;

        if (userData.getEmail().isEmpty()
                /*|| !android.util.Patterns.EMAIL_ADDRESS.matcher(userData.getEmail()).matches()*/) {
            email = "enter a valid email address";
            isValid = false;
        }

        if (userData.getPassword().isEmpty() || userData.getPassword().length() < 4
                || userData.getPassword().length() > 10) {
            password = "between 4 and 10 alphanumeric characters";
            isValid = false;
        }

        getViewState().showValidFieldError(new UserSignIn(email,password));
        return isValid;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
