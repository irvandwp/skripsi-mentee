package com.irvandwiputra.skripsimentee;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.irvandwiputra.skripsimentee.Utility.Constant;
import com.irvandwiputra.skripsimentee.Model.User;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = SignUpActivity.class.getSimpleName();

    @Bind(R.id.textEmail)
    EditText textEMail;

    @Bind(R.id.textName)
    EditText textName;

    @Bind(R.id.textAddress)
    EditText textAddress;

    @Bind(R.id.textPassword)
    EditText textPassword;

    @Bind(R.id.textOccupation)
    EditText textOccupation;

    @Bind(R.id.textPhoneNumber)
    EditText textPhoneNo;

    @Bind(R.id.buttonSubmit)
    Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        buttonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSubmit:
                SignUp();
                break;
        }
    }

    public void SignUp() {
        AlertDialog.Builder builderLoading = new AlertDialog.Builder(this);
        builderLoading.setMessage("Please wait");
        final AlertDialog dialogLoading = builderLoading.create();
        dialogLoading.show();

        User user = new User(textEMail.getText().toString().trim(),
                textName.getText().toString().trim(),
                textPassword.getText().toString().trim(),
                textPhoneNo.getText().toString().trim(),
                textAddress.getText().toString().trim(),
                Constant.ROLE_MENTEE,
                textOccupation.getText().toString().trim());
        Gson gson = new Gson();

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(Constant.MEDIA_TYPE_MARKDOWN, gson.toJson(user));

        Request request = new Request.Builder()
                .url(Constant.URL_NEW_USER)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogLoading.hide();

                            AlertDialog.Builder builderSuccess = new AlertDialog.Builder(SignUpActivity.this);
                            builderSuccess.setMessage("Successfully create new account");
                            builderSuccess.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                            AlertDialog dialogSuccess = builderSuccess.create();
                            dialogSuccess.show();
                        }
                    });
                }
            }
        });
    }
}
