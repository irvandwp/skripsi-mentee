package com.irvandwiputra.skripsimentee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.irvandwiputra.skripsimentee.Constant.CONSTANTS;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LoginActivity.class.getSimpleName();

    @Bind(R.id.buttonSignUp)
    Button buttonSignUp;

    @Bind(R.id.buttonSignIn)
    Button buttonSignIn;

    @Bind(R.id.textLoginEmail)
    EditText textLoginEmail;

    @Bind(R.id.textLoginPassword)
    EditText textLoginPassword;

    @Bind(R.id.pbSignIn)
    ProgressBar pbSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        buttonSignUp.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignUp:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonSignIn:
                DoSignIn();
                break;
        }
    }

    private void DoSignIn() {
        showProgressBar();

        User user = new User();
        user.setEmail(textLoginEmail.getText().toString().trim());
        user.setPassword(textLoginPassword.getText().toString().trim());
        user.setRole(CONSTANTS.ROLE_MENTEE);

        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(CONSTANTS.MEDIA_TYPE_MARKDOWN, gson.toJson(user));

        Log.i(TAG, "DoSignIn: " + gson.toJson(user));

        Request request = new Request.Builder()
                .url(CONSTANTS.URL_LOGIN)
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Log.i(TAG, "onResponse: " + response.code());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    Log.i(TAG, "onFailure: failed to sign in");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressBar();
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Error")
                                    .setMessage("Please enter the correct credentials")
                                    .setPositiveButton("OK", null);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                }
            }
        });

    }

    private void showProgressBar() {
        textLoginEmail.setVisibility(View.INVISIBLE);
        textLoginPassword.setVisibility(View.INVISIBLE);
        buttonSignIn.setVisibility(View.INVISIBLE);
        buttonSignUp.setVisibility(View.INVISIBLE);
        pbSignIn.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        textLoginEmail.setVisibility(View.VISIBLE);
        textLoginPassword.setVisibility(View.VISIBLE);
        buttonSignIn.setVisibility(View.VISIBLE);
        buttonSignUp.setVisibility(View.VISIBLE);
        pbSignIn.setVisibility(View.INVISIBLE);
    }
}
