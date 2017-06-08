package com.irvandwiputra.skripsimentee;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.irvandwiputra.skripsimentee.Model.Order;
import com.irvandwiputra.skripsimentee.Model.ResponseStatus;
import com.irvandwiputra.skripsimentee.Utility.Constant;

import java.io.IOException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = OrderActivity.class.getSimpleName();
    private int hour, minute;

    @Bind(R.id.textLatitude)
    public EditText textLatitude;

    @Bind(R.id.textLongitude)
    public EditText textLongitude;

    @Bind(R.id.textCourseId)
    public EditText textCourseId;

    @Bind(R.id.textStartTime)
    public EditText textStartTime;

    @Bind(R.id.textDuration)
    public EditText textDuration;

    @Bind(R.id.textDescription)
    public EditText textDescription;

    @Bind(R.id.buttonCreate)
    public Button buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        buttonCreate.setOnClickListener(this);
        textStartTime.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCreate:
                DoCreateOrder();
                break;
            case R.id.textStartTime:
                OpenTimePicker();
                break;
        }
    }

    private void OpenTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textStartTime.setText(hourOfDay + ":" + minute);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    public void validateCreateOrder() {

    }

    public void DoCreateOrder() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Order order = new Order();
        order.setCourse_id(Integer.parseInt(textCourseId.getText().toString().trim()));
        order.setOrder_description(textDescription.getText().toString().trim());
        order.setDuration(Integer.parseInt(textDuration.getText().toString().trim()));
        order.setPrice(100000);
        order.setLatitude(Float.parseFloat(textLatitude.getText().toString().trim()));
        order.setLongitude(Float.parseFloat(textLongitude.getText().toString().trim()));
        order.setToken(Constant.getToken(getApplicationContext()));

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.URL_NEW_ORDER)
                .addHeader("Content-Type", "application/json")
                .post(Order.createJSONRequest(order))
                .build();
        Call call = okHttpClient.newCall(request);

        Log.i(TAG, "DoCreateOrder: " + Order.createJSON(order));

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseApi = response.body().string();
                    final ResponseStatus responseStatus = ResponseStatus.parseJSON(responseApi);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.hide();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext())
                                    .setMessage(responseStatus.getMessage())
                                    .setPositiveButton("OK", null);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    response.close();
                }
            }
        });

    }
}
