package com.irvandwiputra.skripsimentee.Utility;

import android.content.Context;

import java.util.regex.Pattern;

import okhttp3.MediaType;

/**
 * Created by Irvan Dwi Putra on 6/1/2017.
 */

public class Constant {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    public static final String ROLE_MENTOR = "mentor";
    public static final String ROLE_MENTEE = "mentee";

    public static final String TOKEN = "TOKEN";

    private static final String BASE_URL = "http://192.168.0.102:8888/skripsi/";
    public static final String URL_NEW_USER = BASE_URL + "api/users/new";
    public static final String URL_LOGIN = BASE_URL + "api/users/login";
    public static final String URL_NEW_ORDER = BASE_URL + "api/orders/new";
    public static final String URL_COURSE = BASE_URL + "api/orders";

    public static String getToken(Context context) {
        TinyDB tinyDB = new TinyDB(context);
        return tinyDB.getString(TOKEN);
    }

    public static void setToken(Context context, String token) {
        TinyDB tinyDB = new TinyDB(context);
        tinyDB.putString(TOKEN, token);
    }

}
