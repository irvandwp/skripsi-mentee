package com.irvandwiputra.skripsimentee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.irvandwiputra.skripsimentee.Model.ResponseStatus;
import com.irvandwiputra.skripsimentee.Model.Token;
import com.irvandwiputra.skripsimentee.Model.User;
import com.irvandwiputra.skripsimentee.Utility.Constant;
import com.irvandwiputra.skripsimentee.Utility.TextWatcher;

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
    public ProgressDialog progressDialog;
    @Bind(R.id.buttonSignUp)
    public Button buttonSignUp;
    @Bind(R.id.buttonSignIn)
    public Button buttonSignIn;
    @Bind(R.id.emailTxtInputLayout)
    public TextInputLayout emailTxtInputLayout;
    @Bind(R.id.textLoginEmail)
    public EditText textLoginEmail;
    @Bind(R.id.passwordTxtInputLayout)
    public TextInputLayout passwordTxtInputLayout;
    @Bind(R.id.textLoginPassword)
    public EditText textLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        textLoginEmail.addTextChangedListener(new TextWatcher(textLoginEmail));
        textLoginPassword.addTextChangedListener(new TextWatcher(textLoginPassword));
        buttonSignUp.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);

        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
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
        if (!validateEmail() || !validatePassword()) return;
        DoSignIn();
    }

    private void DoSignIn() {
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
                    Log.i(TAG, "onResponse: status code " + response.code());
                    if (response.isSuccessful()) {
                        Token token = Token.parseJSON(responseApi);
                        User responseUser = User.parseJSON(responseApi);
                        Constant.setToken(getApplicationContext(), token.getToken());
                        Constant.setEmail(getApplicationContext(), responseUser.getEmail());
                        Constant.setName(getApplicationContext(), responseUser.getName());
                        progressDialog.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                    } else {
                        final ResponseStatus responseStatus = ResponseStatus.parseJSON(responseApi);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
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

    private boolean validateEmail() {
        String email = textLoginEmail.getText().toString().trim();

        if (email.isEmpty()) {
            emailTxtInputLayout.setError("Please enter your email address");
            requestFocus(textLoginEmail);
            return false;
        } else if (!Constant.isValidEmail(email)) {
            emailTxtInputLayout.setError("Please enter a valid email address");
            requestFocus(textLoginEmail);
            return false;
        } else {
            emailTxtInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (textLoginPassword.getText().toString().trim().isEmpty()) {
            passwordTxtInputLayout.setError("Please enter your password");
            requestFocus(textLoginPassword);
            return false;
        } else {
            passwordTxtInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
