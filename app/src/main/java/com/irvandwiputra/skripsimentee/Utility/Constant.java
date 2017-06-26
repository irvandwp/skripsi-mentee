package com.irvandwiputra.skripsimentee.Utility;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import okhttp3.MediaType;

/**
 * Created by Irvan Dwi Putra on 6/1/2017.
 */

public class Constant {

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    public static final String ROLE_MENTOR = "mentor";
    public static final String ROLE_MENTEE = "mentee";

    private static final String TOKEN = "TOKEN";
    private static final String EMAIL = "EMAIL";
    private static final String NAME = "NAME";

    private static final String BASE_URL = "http://192.168.100.3:8888/skripsi/";
    public static final String URL_NEW_USER = BASE_URL + "api/users/new";
    public static final String URL_LOGIN = BASE_URL + "api/users/login";
    public static final String URL_NEW_ORDER = BASE_URL + "api/orderArrayList/new";
    public static final String URL_COURSE = BASE_URL + "api/courses";
    public static final String URL_ORDER = BASE_URL + "api/orders";

    public static final int REQUEST_PERMISSION_REQUEST_CODE = 34;

    public static String getToken(Context context) {
        TinyDB tinyDB = new TinyDB(context);
        return tinyDB.getString(TOKEN);
    }

    public static void setToken(Context context, String token) {
        TinyDB tinyDB = new TinyDB(context);
        tinyDB.putString(TOKEN, token);
    }

    public static String getEmail(Context context) {
        TinyDB tinyDB = new TinyDB(context);
        return tinyDB.getString(EMAIL);
    }

    public static void setEmail(Context context, String email) {
        TinyDB tinyDB = new TinyDB(context);
        tinyDB.putString(EMAIL, email);
    }

    public static String getName(Context context) {
        TinyDB tinyDB = new TinyDB(context);
        return tinyDB.getString(NAME);
    }

    public static void setName(Context context, String name) {
        TinyDB tinyDB = new TinyDB(context);
        tinyDB.putString(NAME, name);
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
