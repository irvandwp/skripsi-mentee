package com.irvandwiputra.skripsimentee.Utility;

import okhttp3.MediaType;

/**
 * Created by Irvan Dwi Putra on 6/1/2017.
 */

public class Constant {

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    public static final String ROLE_MENTOR = "mentor";
    public static final String ROLE_MENTEE = "mentee";

    public static final String TOKEN = "TOKEN";

    private static final String BASE_URL = "http://192.168.100.10:8888/skripsi/";
    public static final String URL_NEW_USER = BASE_URL + "api/users/new";
    public static final String URL_LOGIN = BASE_URL + "api/users/login";

}
