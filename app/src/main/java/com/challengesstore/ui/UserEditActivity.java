package com.challengesstore.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;

import com.challengesstore.R;
import com.challengesstore.data.api.ApiFactory;
import com.challengesstore.data.api.service.UserService;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserEditActivity extends Activity {

    private static final int PICK_IMAGE = 100;

    @BindView(R.id.button)
    Button button;


    public static void start(Context context) {
        Intent intent = new Intent(context,UserEditActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        ButterKnife.bind(this);
        if (button != null) {
            button.setOnClickListener(view -> {

                Call<ResponseBody> call = ApiFactory.getUserService(getApplicationContext()).welcome();
                try {
                    Response<ResponseBody> responseBody = call.execute();
                    Log.i("Call in activity",responseBody.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*Intent intent = new Intent(this,);
                intent.setType("image*//**//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);*//*
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE);*/
            });
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            // get selected image URI from Gallery activity
            Uri selectedImage = data.getData();

            // work with Image paths columns
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            File file = new File(filePath);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "Art");
            RequestBody lastName = RequestBody.create(MediaType.parse("text/plain"), "Art");

            Log.d("THIS", data.getData().getPath());

            UserService registerService = ApiFactory.getUserService(getApplicationContext());
            Call<ResponseBody> req = registerService.postImage(body, name,lastName);

            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Log.i("ResponseImage",response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }
}
