package com.challengesstore;

import android.support.annotation.NonNull;

import com.challengesstore.data.repository.RegisterRepository;
import com.challengesstore.data.model.register.UserSignIn;
import com.challengesstore.ui.login.LoginPresenter;
import com.challengesstore.ui.login.LoginView$$State;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import okhttp3.ResponseBody;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by felix on 1/10/18
 */
// TODO : REWRITE THIS BULL-SHIT
public class SignInPresenterTest {

    public static final String FAKE_API_USER = "USER_ARTEM@51151";

    @Mock
    RegisterRepository repository;

    @Mock
    LoginView$$State view;

    private LoginPresenter presenter;

    @Rule
    public SchedulerRxRule schedulerRxRule = new SchedulerRxRule();


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        presenter = new LoginPresenter(repository);
        presenter.setViewState(view);
    }


    @Test
    public void signInCorrect_Test() {

        when(repository.signIn(anyString()))
                .thenReturn(schedulerRxRule.getSuccessObservable(getSuccessResponseBody()));
        presenter.signIn(getValidSignInUser());

        verify(view).setButtonEnabled(false);

    }

    @Test
    public void signInNotCorrect_Test() {

        when(repository.signIn(anyString()))
                .thenReturn(schedulerRxRule.getErrorObservable(401,"Email error"));

        presenter.signIn(getNotValidSignInUser());

        verify(view).setButtonEnabled(true);
        verify(view).showValidFieldError(new UserSignIn("enter a valid email address",
                "between 4 and 10 alphanumeric characters"));
        verifyNoMoreInteractions(view);
    }

    @Test
    public void onSuccessResponse_Test() {
        when(repository.signIn(new Gson().toJson(getValidSignInUser())))
                .thenReturn(schedulerRxRule.getSuccessObservable(getSuccessResponseBody()));

        presenter.sendUserData(getValidSignInUser());

        verify(view).onResponseSuccess(FAKE_API_USER);

    }

    @Test
    public void onErrorResponse_Test() {
        when(repository.signIn(anyString()))
                .thenReturn(schedulerRxRule.getErrorObservable(501,"User doesn't exist"));

        presenter.signIn(getValidSignInUser());

        verify(view).onResponseError("User doesn't exist");

    }

    @Test
    public void userDataValid_Test() {
        assertTrue(presenter.isUserDataValid(getValidSignInUser()));

        // null return when field valid
        verify(view).showValidFieldError(new UserSignIn(null,null));
    }

    @Test
    public void userDataNotValid_Test() {
        assertFalse(presenter.isUserDataValid(getNotValidSignInUser()));

        verify(view).showValidFieldError(new UserSignIn("enter a valid email address"
                ,"between 4 and 10 alphanumeric characters"));
    }



    // Stub properly response
    @NonNull
    private ResponseBody getSuccessResponseBody() {
        return ResponseBody.create(null,FAKE_API_USER);
    }

    // STUBS
    @NonNull
    private UserSignIn getValidSignInUser() {
        return new UserSignIn("artem@gmail.com","sqqasdq");
    }


    @NonNull
    private UserSignIn getNotValidSignInUser() {
        return new UserSignIn("","ssz");
    }
}
