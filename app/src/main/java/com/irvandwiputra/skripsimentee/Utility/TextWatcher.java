package com.irvandwiputra.skripsimentee.Utility;

import android.text.Editable;
import android.view.View;

import com.irvandwiputra.skripsimentee.R;

/**
 * Created by alvinoktavianus on 6/23/17.
 */

public class TextWatcher implements android.text.TextWatcher {
    private View view;

    public TextWatcher(View view) {
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        switch (view.getId()) {
            case R.id.textDescription:
                break;
            case R.id.textStartTime:
                break;
            case R.id.textDuration:
                break;
            case R.id.textEmail:
                break;
            case R.id.textName:
                break;
            case R.id.textPassword:
                break;
            case R.id.textAddress:
                break;
            case R.id.textPhoneNumber:
                break;
            case R.id.textOccupation:
                break;
            case R.id.textLoginEmail:
                break;
            case R.id.textLoginPassword:
                break;
        }
    }
}
