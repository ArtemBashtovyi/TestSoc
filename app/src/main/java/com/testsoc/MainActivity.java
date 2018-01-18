package com.testsoc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.testsoc.ui.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String KEY = "keyLogin";

    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.name_text_view)
    TextView nameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LoginActivity.start(this);

       // UserEditActivity.start(this);
        //SignUpActivity.start(this);
        //finish();

        String emulateUser = getIntent().getStringExtra(KEY);
        nameTv.setText(emulateUser);

    }
}
