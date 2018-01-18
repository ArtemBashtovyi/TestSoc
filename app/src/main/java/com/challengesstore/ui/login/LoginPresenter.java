package com.challengesstore.ui.login;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.challengesstore.data.model.register.UserSignIn;
import com.challengesstore.data.model.tokens.AuthResponse;
import com.challengesstore.data.repository.AuthRepository;
import com.challengesstore.data.repository.RegisterRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginView> {

    private RegisterRepository repository;
    private AuthRepository authRepository;

    private CompositeDisposable disposable = new CompositeDisposable();

    public LoginPresenter() {
    }

    public LoginPresenter(RegisterRepository repository,AuthRepository authRepository) {
        this.repository = repository;
        this.authRepository = authRepository;
    }

    void onButtonSignInClick(){
        getViewState().signIn();
    }

    public void signIn(UserSignIn userData) {

        if (!isUserDataValid(userData)) {
            getViewState().setButtonEnabled(true);
            return;
        }

        sendUserData(userData);
        getViewState().setButtonEnabled(false);
    }

    private void sendUserData(UserSignIn user) {
        // clear previous observables if not first time click
        disposable.clear();

        disposable.add(repository
                .signIn(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {
                    if (!responseBody.isSuccessful()) {
                        getViewState().onResponseError(responseBody.errorBody().string());
                        return;
                    }

                    onResponseSuccess(responseBody.body());
                   // Log.i("ResponseFull", responseBody.body().getAccessToken());

                }, e -> getViewState().onResponseError(e.toString())));
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
                || userData.getPassword().length() > 30) {
            password = "between 4 and 10 alphanumeric characters";
            isValid = false;
        }

        getViewState().showValidFieldError(new UserSignIn(email,password));
        return isValid;
    }


    private void onResponseSuccess(AuthResponse authResponse) {

        if (!authResponse.getAccessToken().isEmpty()
                && !authResponse.getRefreshToken().isEmpty()
                && authResponse.getId() > 0) {

           // Log.i("Tokens","Tokens are successfully created");

            authRepository.setId(authResponse.getId());
            authRepository.setRefreshToken(authResponse.getRefreshToken());
            authRepository.setAccessToken(authResponse.getAccessToken());
            getViewState().onResponseSuccess();

        } else Log.i("Tokens", "Tokens are empty!!!!!!!!!!!");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
