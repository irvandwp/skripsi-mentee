package com.irvandwiputra.skripsimentee;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.irvandwiputra.skripsimentee.Model.Course;
import com.irvandwiputra.skripsimentee.Model.Order;
import com.irvandwiputra.skripsimentee.Model.ResponseStatus;
import com.irvandwiputra.skripsimentee.Utility.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderActivity extends AppCompatActivity
        implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, Spinner.OnItemSelectedListener, View.OnFocusChangeListener {

    public static final String TAG = OrderActivity.class.getSimpleName();
    public ArrayList<String> stringArrayList = new ArrayList<>();
    public Course[] courses;
    public int courseId;

    @Bind(R.id.textLatitude)
    public EditText textLatitude;

    @Bind(R.id.textLongitude)
    public EditText textLongitude;

    @Bind(R.id.textStartTime)
    public EditText textStartTime;

    @Bind(R.id.textDuration)
    public EditText textDuration;

    @Bind(R.id.textDescription)
    public EditText textDescription;

    @Bind(R.id.buttonCreate)
    public Button buttonCreate;

    @Bind(R.id.spinnerCourse)
    public Spinner spinnerCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        initializeCourseList();

        buttonCreate.setOnClickListener(this);
        textStartTime.setOnFocusChangeListener(this);
    }

    private void initializeCourseList() {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Constant.URL_COURSE)
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
                    Log.i(TAG, "onResponse: " + responseApi);
                    if (response.isSuccessful()) {
                        courses = Course.parseJSONArray(responseApi);
                        stringArrayList.add("-- Select course --");
                        for (Course course : courses) {
                            stringArrayList.add(course.getName());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                spinnerCourse.setAdapter(new ArrayAdapter<String>(OrderActivity.this, android.R.layout.simple_spinner_dropdown_item, stringArrayList));
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

    public String getCourseName(int position) {
        return courses[position].getName();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCreate:
                DoCreateOrder();
                break;
        }
    }

    private void OpenTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, true);
        timePickerDialog.show();
    }

    public void validateCreateOrder() {

    }

    public void DoCreateOrder() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Order order = new Order();
        order.setCourse_id(courseId);
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

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        textStartTime.setText(hourOfDay + ":" + minute);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String name = getCourseName(position);
        for (Course course : courses) {
            if (course.getName().equals(name)) {
                courseId = course.getId();
            } else {
                courseId = 0;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.textStartTime:
                if (hasFocus) OpenTimePicker();
                break;
        }
    }
}
