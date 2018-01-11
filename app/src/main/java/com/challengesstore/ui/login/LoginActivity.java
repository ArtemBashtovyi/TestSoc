package com.challengesstore.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.challengesstore.MainActivity;
import com.challengesstore.R;
import com.challengesstore.data.RegisterRepository;
import com.challengesstore.data.model.registration.UserSignIn;
import com.challengesstore.ui.signup.SignUpActivity;

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

    @InjectPresenter
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

        RegisterRepository repository = new RegisterRepository();
        presenter = new LoginPresenter(repository);

        signInBn.setOnClickListener(view -> presenter.onButtonSignInClick());

        linkSignUpTv.setOnClickListener(view -> SignUpActivity.start(this));
    }



    @Override
    public void signIn() {
        Log.d(TAG, "Login");

        final String email = emailEt.getText().toString();
        final String password = passwordEt.getText().toString();

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
    public void onResponseError(String errorText) {
        Toast.makeText(getBaseContext(), errorText, Toast.LENGTH_LONG).show();
        signInBn.setEnabled(true);
    }

    @Override
    public void onResponseSuccess(String response) {
        // TODO : caching unique USER_API_KEY
        Toast.makeText(getBaseContext(), "Login success", Toast.LENGTH_LONG).show();
        MainActivity.start(this,response);
        signInBn.setEnabled(true);
        finish();
    }
}
