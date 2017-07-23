package com.irvandwiputra.skripsimentee;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyRating extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.btnSubmit)
    public Button buttonSubmitRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rating);

        ButterKnife.bind(this);
        buttonSubmitRating.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage("Successfully submit your rating. Thank you")
                        .setPositiveButton("OK", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
    }
}
