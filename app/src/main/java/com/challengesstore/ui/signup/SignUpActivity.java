package com.challengesstore.ui.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.challengesstore.R;
import com.challengesstore.data.repository.RegisterRepository;
import com.challengesstore.data.model.register.UserSignUp;
import com.challengesstore.ui.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements SignUpView {

    private static final String TAG = "SignUpActivity";

    @BindView(R.id.input_name)
    EditText nameEt;

    @BindView(R.id.input_surname)
    EditText surnameEt;

    @BindView(R.id.input_email)
    EditText emailEt;

    @BindView(R.id.input_password)
    EditText passwordEt;

    @BindView(R.id.input_user_name)
    EditText userNameEt;

    @BindView(R.id.input_password_repeat)
    EditText passwordRepeatEt;

    @BindView(R.id.link_login)
    TextView linkLoginTv;

    @BindView(R.id.btn_signup)
    Button signUpBn;

    private SignUpPresenter presenter;

    public static void start(Context context) {
        Intent intent = new Intent(context,SignUpActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        RegisterRepository repository = new RegisterRepository();
        presenter = new SignUpPresenter(this,repository);

        signUpBn.setOnClickListener(view -> presenter.onClick());

        linkLoginTv.setOnClickListener(view -> LoginActivity.start(this));
    }


    @Override
    public void onResponseSuccess() {
        Toast.makeText(getBaseContext(), "Login success", Toast.LENGTH_LONG).show();
        signUpBn.setEnabled(true);
        LoginActivity.start(this);
        finish();
    }

    @Override
    public void onResponseError(String errorText) {
        Toast.makeText(getBaseContext(), errorText, Toast.LENGTH_LONG).show();
        Log.e("ErrorResponse-",errorText);
        signUpBn.setEnabled(true);
    }

    //
    @Override
    public void signUp() {
        String name = nameEt.getText().toString();
        String surname = surnameEt.getText().toString();
        String userName = userNameEt.getText().toString();
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        String passwordRepeat = passwordRepeatEt.getText().toString();

        UserSignUp userData = new UserSignUp(name,surname,userName,email,password,passwordRepeat);
        presenter.signUp(userData);

        Log.i(TAG,userData.toString());
    }


    @Override
    public void showValidFieldError(UserSignUp userData) {
        nameEt.setError(userData.getName());
        surnameEt.setError(userData.getSurname());
        userNameEt.setError(userData.getUserName());
        emailEt.setError(userData.getEmail());
        passwordEt.setError(userData.getPassword());
        passwordRepeatEt.setError(userData.getPasswordRepeat());
    }

    @Override
    public void setButtonEnabled(boolean isEnabled) {
        signUpBn.setEnabled(isEnabled);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}


