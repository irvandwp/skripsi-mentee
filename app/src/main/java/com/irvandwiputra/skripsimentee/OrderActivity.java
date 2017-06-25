package com.irvandwiputra.skripsimentee;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.irvandwiputra.skripsimentee.Model.Course;
import com.irvandwiputra.skripsimentee.Model.Order;
import com.irvandwiputra.skripsimentee.Model.ResponseStatus;
import com.irvandwiputra.skripsimentee.Utility.Constant;
import com.irvandwiputra.skripsimentee.Utility.TextWatcher;

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
        implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, Spinner.OnItemSelectedListener, View.OnFocusChangeListener, OnMapReadyCallback {

    public static final String TAG = OrderActivity.class.getSimpleName();
    public ArrayList<String> stringArrayList = new ArrayList<>();
    public Course[] courses;
    public int courseId;
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    public GoogleMap googleMap;

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

    @Bind(R.id.descriptionTxtInputLayout)
    public TextInputLayout descriptionInputLayout;

    @Bind(R.id.durationTxtInputLayout)
    public TextInputLayout durationInputLayout;

    @Bind(R.id.startTimeTxtInputLayout)
    public TextInputLayout startTimeInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        initializeCourseList();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        textDescription.addTextChangedListener(new TextWatcher(textDescription));
        textStartTime.addTextChangedListener(new TextWatcher(textStartTime));
        textDuration.addTextChangedListener(new TextWatcher(textDuration));

        buttonCreate.setOnClickListener(this);
        textStartTime.setOnFocusChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            latitude = mLastLocation.getLatitude();
                            longitude = mLastLocation.getLongitude();
                            initializeGoogleMap();
                            Log.i(TAG, "onComplete: " + mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude());
                        } else {
                            Log.w(TAG, "getLastLocationException", task.getException());
                        }
                    }
                });
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(OrderActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constant.REQUEST_PERMISSION_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            startLocationPermissionRequest();
        } else {
            Log.i(TAG, "Requesting permission");
            startLocationPermissionRequest();
        }
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void initializeGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.orderMap);
        mapFragment.getMapAsync(this);
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
                                spinnerCourse.setAdapter(new ArrayAdapter<>(OrderActivity.this, android.R.layout.simple_spinner_dropdown_item, stringArrayList));
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
                validateCreateOrder();
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateDescription() {
        if (textDescription.getText().toString().trim().isEmpty()) {
            descriptionInputLayout.setError("This field is required");
            requestFocus(textDescription);
            return false;
        } else {
            descriptionInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDuration() {
        String duration = textDuration.getText().toString().trim();
        int durationNumber = Integer.parseInt(duration);
        if (duration.isEmpty()) {
            durationInputLayout.setError("This field is required");
            requestFocus(textDuration);
            return false;
        } else if (durationNumber < 1 || durationNumber > 4) {
            durationInputLayout.setError("Please enter a number from 1 until 4");
            requestFocus(textDuration);
            return false;
        } else {
            durationInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateStartTime() {
        if (textStartTime.getText().toString().trim().isEmpty()) {
            startTimeInputLayout.setError("This field is required");
            requestFocus(textStartTime);
            return false;
        } else {
            startTimeInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public void validateCreateOrder() {
        if (!validateDescription() || !validateStartTime() || !validateDuration()) return;
        if (courseId == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("Please select course")
                    .setPositiveButton("OK", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        DoCreateOrder();
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
        order.setLatitude(latitude);
        order.setLongitude(longitude);
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
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    });
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng menteeLocation = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(menteeLocation).title("Your current location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(menteeLocation, 16f));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult: entering result code");
        if (requestCode == Constant.REQUEST_PERMISSION_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}
