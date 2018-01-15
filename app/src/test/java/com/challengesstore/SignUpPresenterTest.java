package com.challengesstore;

import android.support.annotation.NonNull;
import android.util.Patterns;

import com.challengesstore.data.repository.RegisterRepository;
import com.challengesstore.data.model.register.UserSignUp;
import com.challengesstore.data.model.register.error.DataValidationError;
import com.challengesstore.ui.signup.SignUpPresenter;
import com.challengesstore.ui.signup.SignUpView;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import okhttp3.ResponseBody;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by felix on 1/8/18
 */

public class SignUpPresenterTest {

    private static final String NOT_VALID_RESPONSE = "{ \"errors\":{ \"username\":\"Error\", \"email\":\"email already used\" } }";

    @Mock
    SignUpView view;

    @Mock
    RegisterRepository registerRepository;

    @Rule
    public SchedulerRxRule rxRule = new SchedulerRxRule();

    @Mock
    Patterns patterns;

    private SignUpPresenter presenter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        presenter = new SignUpPresenter(view,registerRepository);
    }


    // Test for deserialization if in modelPojo more field then in json
    @Test
    public void deserializationUserSignUp_Test(){

        DataValidationError error;

        Gson gson = new Gson();
        error = gson.fromJson(NOT_VALID_RESPONSE,DataValidationError.class);
        System.out.println(error.getUserSignUp().toString());

        assertTrue(error.getUserSignUp().equals(new UserSignUp(null,null,"Error","email already used",null,null)));

    }


    // Test success response from server
    @Test
    public void onSuccessResponse_Test() {

        Gson gson = new Gson();
        String json = gson.toJson(getTestUser());

        when(registerRepository.signUp(json))
                .thenReturn(rxRule.getSuccessObservable(getSuccessResponseBody()));

        presenter.signUp(getTestUser());

        verify(view).onResponseSuccess();

    }

    // Test error response from server
    @Test
    public void onErrorResponse_Test() {



        when(registerRepository.signUp(anyString()))
                .thenReturn(rxRule.getErrorObservable(400,NOT_VALID_RESPONSE));

        System.out.println();
        presenter.signUp(getTestUser());

        verify(view).setButtonEnabled(false);
        verify(view).showValidFieldError(new UserSignUp(null,null,"Error","email already used",null));

        System.out.println(NOT_VALID_RESPONSE);

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
        return new UserSignUp("Linus","Torvalds","Linux","gnu_linux_best_os_ever@mail.ru","asda2","asda2");
    }


}
