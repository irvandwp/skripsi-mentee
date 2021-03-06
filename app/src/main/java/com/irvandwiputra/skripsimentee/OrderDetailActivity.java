package com.irvandwiputra.skripsimentee;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.irvandwiputra.skripsimentee.Model.Order;
import com.irvandwiputra.skripsimentee.Model.ResponseStatus;
import com.irvandwiputra.skripsimentee.Model.Token;
import com.irvandwiputra.skripsimentee.Utility.Constant;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = OrderDetailActivity.class.getSimpleName();
    Order order;
    ResponseStatus responseStatus;

    @Bind(R.id.buttonRate)
    public Button buttonRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Bundle bundle = getIntent().getExtras();
        String orderId = bundle.getString("orderId");

        DoGetOrderDetail(orderId);
        ButterKnife.bind(this);
        buttonRate.setOnClickListener(this);
    }

    private void DoGetOrderDetail(String id) {
        OkHttpClient okHttpClient = new OkHttpClient();

        Token token = new Token(Constant.getToken(this));

        Request request = new Request.Builder()
                .url(Constant.URL_ORDER_DETAIL + "/id")
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
                    String responseApi = response.body().string();
                    if (response.isSuccessful()) {
                        order = Order.parseSingleJSON(responseApi);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // jalankan code untuk attach data ke dalam view
                            }
                        });
                    } else {
                        responseStatus = ResponseStatus.parseJSON(responseApi);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // jalankan code untuk munculin alertdialog
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRate:
                Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
