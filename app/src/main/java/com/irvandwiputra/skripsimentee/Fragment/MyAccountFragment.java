package com.irvandwiputra.skripsimentee.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irvandwiputra.skripsimentee.Model.Token;
import com.irvandwiputra.skripsimentee.Model.User;
import com.irvandwiputra.skripsimentee.R;
import com.irvandwiputra.skripsimentee.Utility.Constant;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {

    public static final String TAG = MyAccountFragment.class.getSimpleName();
    private ProgressDialog progressDialog;
    private User user;

    @Bind(R.id.emailEditText)
    public TextInputEditText email;
    @Bind(R.id.fullnameEditText)
    public TextInputEditText fullname;
    @Bind(R.id.addressEditText)
    public TextInputEditText address;
    @Bind(R.id.mobileEditText)
    public TextInputEditText mobilephone;
    @Bind(R.id.occupationEditText)
    public TextInputEditText occupation;

    public MyAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
        ButterKnife.bind(this, view);
        getCurrentUser();

        return view;
    }

    public void getCurrentUser() {
        OkHttpClient okHttpClient = new OkHttpClient();

        Token token = new Token(Constant.getToken(getContext()));

        Request request = new Request.Builder()
                .url(Constant.URL_CURRENT_USER)
                .addHeader("Content-Type", "application/json")
                .post(Token.createJSONRequest(token))
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseData = response.body().string();
                    Log.i(TAG, "onResponse: " + response.code());
                    Log.i(TAG, "onResponse: " + responseData);
                    if (response.isSuccessful()) {
                        user = User.parseJSON(responseData);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                email.setText(user.getEmail());
                                fullname.setText(user.getName());
                                address.setText(user.getAddress());
                                mobilephone.setText(user.getPhone());
                                occupation.setText(user.getOccupation());
                            }
                        });
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } finally {
                    response.close();
                }
            }
        });
    }

}
