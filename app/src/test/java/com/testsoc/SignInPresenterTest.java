package com.testsoc;

import android.support.annotation.NonNull;

import com.testsoc.data.model.register.UserSignIn;
import com.testsoc.data.model.tokens.AuthResponse;
import com.testsoc.data.repository.AuthRepository;
import com.testsoc.data.repository.RegisterRepository;
import com.testsoc.ui.login.LoginPresenter;
import com.testsoc.ui.login.LoginView$$State;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by felix on 1/10/18
 */
// TODO : REWRITE THIS BULL-SHIT
public class SignInPresenterTest {

    public static final String FAKE_API_USER = "USER_ARTEM@51151";

    @Rule
    public SchedulerRxRule schedulerRxRule = new SchedulerRxRule();

    @Mock
    RegisterRepository repository;


    @Mock
    AuthRepository authRepository;


    @Mock
    LoginView$$State view;

    private LoginPresenter presenter;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        presenter = new LoginPresenter(repository,authRepository);
        presenter.setViewState(view);
    }


    @Test
    public void signInCorrect_Test() {

        when(repository.signIn(any()))
                .thenReturn(schedulerRxRule.
                        getSuccessObservable(getAuthResponse()));

        doNothing().when(authRepository).setId(getAuthResponse().getId());
        doNothing().when(authRepository).setAccessToken(getAuthResponse().getAccessToken());
        doNothing().when(authRepository).setRefreshToken(getAuthResponse().getRefreshToken());

        presenter.signIn(getValidSignInUser());

        verify(view).setButtonEnabled(false);
        verify(view).onResponseSuccess();
    }

    @Test
    public void signInNotCorrect_Test() {

        when(repository.signIn(getNotValidSignInUser()))
                .thenReturn(schedulerRxRule.getErrorObservable(401,"Email error"));

        presenter.signIn(getNotValidSignInUser());

        verify(view).setButtonEnabled(true);
        verify(view).showValidFieldError(new UserSignIn("enter a valid email address",
                "between 4 and 10 alphanumeric characters"));
        verifyNoMoreInteractions(view);
    }

    @Test
    public void onErrorResponse_Test() {
        when(repository.signIn(any()))
                .thenReturn(schedulerRxRule.getErrorObservable(501,"User doesn't exist"));

        presenter.signIn(getValidSignInUser());

        verify(view).onResponseError("User doesn't exist");
    }



    // Stub properly response
    @NonNull
    private AuthResponse getAuthResponse() {
        return new AuthResponse(2,"ASDAwasdasD","ADAAasdasd");
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
