package com.testsoc.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.testsoc.R;
import com.testsoc.data.model.register.UserSignIn;
import com.testsoc.data.prefs.PrefManager;
import com.testsoc.data.repository.AuthRepository;
import com.testsoc.data.repository.RegisterRepository;
import com.testsoc.ui.UserEditActivity;
import com.testsoc.ui.signup.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends MvpAppCompatActivity implements LoginView {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.login_input_email)
    EditText emailEt;

    @BindView(R.id.login_input_password)
    EditText passwordEt;

    @BindView(R.id.btn_signin)
    Button signInBn;

    @BindView(R.id.link_signup)
    TextView linkSignUpTv;

    @InjectPresenter(type = PresenterType.LOCAL)
    LoginPresenter presenter;

    public static void start(Context context) {
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        signInBn.setOnClickListener(view -> presenter.onButtonSignInClick());

        linkSignUpTv.setOnClickListener(view -> SignUpActivity.start(this));
    }

    @ProvidePresenter(type = PresenterType.LOCAL)
    LoginPresenter provideDetailPresenter() {

        PrefManager prefManager = PrefManager.getInstance(getApplicationContext());
        AuthRepository authRepository = new AuthRepository(prefManager);

        return new LoginPresenter(new RegisterRepository(),authRepository);
    }

    @Override
    public void signIn() {
        Log.d(TAG, "Login");

        final String email = emailEt.getText().toString();
        final String password = passwordEt.getText().toString();

        // send UI data to presenter
        UserSignIn userData = new UserSignIn(email,password);
        presenter.signIn(userData);
    }

    @Override
    public void showValidFieldError(UserSignIn userData) {
        emailEt.setError(userData.getEmail());
        passwordEt.setError(userData.getPassword());
    }

    @Override
    public void setButtonEnabled(boolean isEnabled) {
        signInBn.setEnabled(isEnabled);
    }

    @Override
    public void onResponseError(@NonNull String errorText) {
        Toast.makeText(getBaseContext(), errorText, Toast.LENGTH_LONG).show();
        signInBn.setEnabled(true);
    }

    @Override
    public void onResponseSuccess() {
        Toast.makeText(getBaseContext(), "Login success", Toast.LENGTH_LONG).show();
        UserEditActivity.start(this);
        signInBn.setEnabled(true);
        finish();
    }
}
