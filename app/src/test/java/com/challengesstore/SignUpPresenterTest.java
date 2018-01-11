package com.challengesstore;

import android.support.annotation.NonNull;
import android.util.Patterns;

import com.challengesstore.data.RegisterRepository;
import com.challengesstore.data.model.registration.UserSignUp;
import com.challengesstore.ui.signup.SignUpPresenter;
import com.challengesstore.ui.signup.SignUpView;
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
import static org.mockito.Mockito.when;

/**
 * Created by felix on 1/8/18
 */

public class SignUpPresenterTest {

    @Mock
    SignUpView view;

    @Mock
    RegisterRepository registerRepository;

    @Rule
    public SchedulerRxRule rxRule = new SchedulerRxRule();

    @Mock
    Patterns patterns;

    SignUpPresenter presenter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        presenter = new SignUpPresenter(view,registerRepository);
    }


    @Test
    public void singUpCorrect_Test() {

        when(registerRepository.signUp(anyString())).thenReturn(rxRule.
                getSuccessObservable(getSuccessResponseBody()));

        presenter.signUp(getTestUser());

        verify(view).setButtonEnabled(false);
        verify(view).onResponseSuccess();

    }

    @Test
    public void singUpUnCorrect_Test() {

        when(registerRepository.signUp(anyString()))
                .thenReturn(rxRule.getErrorObservable(412,"User already exists"));

        presenter.signUp(getNotValidTestUser());
        verify(view).setButtonEnabled(true);
    }

    // verify showing error when data valid
    @Test
    public void onValidData_Test() {

        // doesn't word TODO : delete
        when(Patterns.EMAIL_ADDRESS.matcher(anyString()).matches()).thenReturn(true);

        assertTrue(presenter.isUserDataValid(getTestUser()));
        verify(view).showValidFieldError(new UserSignUp(null,null,null,null,null));
    }


    // verify showing error when data not valid
    @Test
    public void onNotValidData_Test() {
        assertFalse(presenter.isUserDataValid(getNotValidTestUser()));
        verify(view).showValidFieldError(new UserSignUp(null,null,"enter a valid email address",
                null,"passwords doesn't match"));

    }


    // Test success response from server
    @Test
    public void onSuccessResponse_Test() {

        Gson gson = new Gson();
        String json = gson.toJson(getTestUser());

        when(registerRepository.signUp(json))
                .thenReturn(rxRule.getSuccessObservable(getSuccessResponseBody()));

        presenter.sendUserData(getTestUser());

        verify(view).onResponseSuccess();
    }

    // Test error response from server
    @Test
    public void onErrorResponse_Test() {
        String errorTest = "User already exists!";

        // replaced by rxRule method
        /*Response<ResponseBody> response = Response.error(404,ResponseBody.create(null,errorTest));
        Observable<Response<ResponseBody>> observable = Observable.just(response);*/

        when(registerRepository.signUp(anyString()))
                .thenReturn(rxRule.getErrorObservable(408,errorTest));

        presenter.sendUserData(getTestUser());
        verify(view).onResponseError(errorTest);

        assertTrue("error body from server - " + errorTest,true);

    }



    ///////////////////////////////////////////////////////***STUBS***//////////////////////////////////////////////

    // Stub properly response
    @NonNull
    private ResponseBody getSuccessResponseBody() {
        return ResponseBody.create(null,"Successfully registration");
    }

    // Stub properly user
    @NonNull
    private UserSignUp getTestUser() {
        return new UserSignUp("Linus","Torvalds","gnu_linux_best_os_ever@mail.ru","asda2","asda2");
    }

    // Stub improperly user
    @NonNull
    private UserSignUp getNotValidTestUser() {
        return new UserSignUp("aa","zs","","pass","pass_not_same");
    }

}
