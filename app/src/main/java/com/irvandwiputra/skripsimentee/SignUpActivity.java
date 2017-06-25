package com.irvandwiputra.skripsimentee;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
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
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = SignUpActivity.class.getSimpleName();

    @Bind(R.id.textEmail)
    public EditText textEmail;
    @Bind(R.id.textName)
    public EditText textName;
    @Bind(R.id.textAddress)
    public EditText textAddress;
    @Bind(R.id.textPassword)
    public EditText textPassword;
    @Bind(R.id.textOccupation)
    public EditText textOccupation;
    @Bind(R.id.textPhoneNumber)
    public EditText textPhoneNo;
    @Bind(R.id.buttonSubmit)
    public Button buttonSubmit;
    @Bind(R.id.txtEmailLayout)
    public TextInputLayout txtEmailLayout;
    @Bind(R.id.txtNameLayout)
    public TextInputLayout txtNameLayout;
    @Bind(R.id.txtAddressLayout)
    public TextInputLayout txtAddressLayout;
    @Bind(R.id.txtPasswordLayout)
    public TextInputLayout txtPasswordLayout;
    @Bind(R.id.txtOccupationLayout)
    public TextInputLayout txtOccupationLayout;
    @Bind(R.id.txtPhoneLayout)
    public TextInputLayout txtPhoneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        textEmail.addTextChangedListener(new TextWatcher(textEmail));
        textName.addTextChangedListener(new TextWatcher(textName));
        textAddress.addTextChangedListener(new TextWatcher(textAddress));
        textOccupation.addTextChangedListener(new TextWatcher(textOccupation));
        textPhoneNo.addTextChangedListener(new TextWatcher(textPhoneNo));
        textPassword.addTextChangedListener(new TextWatcher(textPassword));

        buttonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSubmit:
                validateSignUp();
                break;
        }
    }

    private boolean validateEmail() {
        String email = textEmail.getText().toString().trim();
        if (email.isEmpty()) {
            txtEmailLayout.setError("Please enter your email address");
            requestFocus(textEmail);
            return false;
        } else if (!Constant.isValidEmail(email)) {
            txtEmailLayout.setError("Please enter a valid email address");
            requestFocus(textEmail);
            return false;
        } else {
            txtEmailLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateName() {
        String name = textName.getText().toString().trim();
        if (name.isEmpty()) {
            txtNameLayout.setError("Please enter your name");
            requestFocus(textName);
            return false;
        } else {
            txtNameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        String password = textPassword.getText().toString().trim();
        if (password.isEmpty()) {
            txtPasswordLayout.setError("Please enter your password");
            requestFocus(textPassword);
            return false;
        } else {
            txtNameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePhoneNo() {
        String phoneno = textPhoneNo.getText().toString().trim();
        if (phoneno.isEmpty()) {
            txtPhoneLayout.setError("Please enter your phone number");
            requestFocus(textPhoneNo);
            return false;
        } else {
            txtPhoneLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateAddress() {
        String address = textAddress.getText().toString().trim();
        if (address.isEmpty()) {
            txtAddressLayout.setError("Please enter your address");
            requestFocus(textAddress);
            return false;
        } else {
            txtAddressLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateOccupation() {
        String occupation = textOccupation.getText().toString().trim();
        if (occupation.isEmpty()) {
            txtOccupationLayout.setError("Please enter your occupation");
            requestFocus(textOccupation);
            return false;
        } else {
            txtOccupationLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void validateSignUp() {
        if (!validateEmail() || !validateName() ||
                !validatePassword() || !validatePhoneNo() ||
                !validateAddress() || !validatePhoneNo() ||
                !validateOccupation())
            return;
        SignUp();
    }

    public void SignUp() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        User user = new User(textEmail.getText().toString().trim(),
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
                            progressDialog.hide();
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
