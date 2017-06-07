package com.irvandwiputra.skripsimentee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.irvandwiputra.skripsimentee.Model.ResponseStatus;
import com.irvandwiputra.skripsimentee.Model.Token;
import com.irvandwiputra.skripsimentee.Model.User;
import com.irvandwiputra.skripsimentee.Utility.Constant;
import com.irvandwiputra.skripsimentee.Utility.TinyDB;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
                validateSignIn();
                break;
        }
    }

    private void validateSignIn() {
        String email = textLoginEmail.getText().toString().trim();
        String password = textLoginPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                    .setMessage("Please fill out all of the required field")
                    .setPositiveButton("OK", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            DoSignIn();
        }
    }

    private void DoSignIn() {

        final ProgressDialog progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        User user = new User();
        user.setEmail(textLoginEmail.getText().toString().trim());
        user.setPassword(textLoginPassword.getText().toString().trim());
        user.setRole(Constant.ROLE_MENTEE);

        OkHttpClient client = new OkHttpClient();

        Log.i(TAG, "DoSignIn: " + User.createJSON(user));

        Request request = new Request.Builder()
                .url(Constant.URL_LOGIN)
                .addHeader("Content-Type", "application/json")
                .post(User.createJSONRequest(user))
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    String responseApi = response.body().string();
                    if (response.isSuccessful()) {
                        Log.i(TAG, "onResponse: success to sign in");
                        Token token = Token.parseJSON(responseApi);

                        TinyDB tinyDB = new TinyDB(getApplicationContext());
                        tinyDB.putString(Constant.TOKEN, token.getToken());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                    } else {
                        Log.i(TAG, "onFailure: failed to sign in");
                        final ResponseStatus responseStatus = ResponseStatus.parseJSON(responseApi);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.hide();
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                                        .setMessage(responseStatus.getMessage())
                                        .setPositiveButton("OK", null);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    response.close();
                }
            }
        });

    }

}
