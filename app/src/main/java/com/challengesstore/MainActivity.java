package com.challengesstore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.challengesstore.ui.signup.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String KEY = "keyLogin";

    public static void start(@NonNull Context context, @NonNull String content) {
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra(KEY,content);
        context.startActivity(intent);
    }

    @BindView(R.id.name_text_view)
    TextView nameTv;

    /* TODO  1) Check errors in SignIn UserSignUp activity 2) Api-key cache

    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SignUpActivity.start(this);
        //finish();

        String emulateUser = getIntent().getStringExtra(KEY);
        nameTv.setText(emulateUser);

    }
}
