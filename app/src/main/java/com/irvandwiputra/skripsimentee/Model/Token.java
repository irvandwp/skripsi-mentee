package com.irvandwiputra.skripsimentee.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.irvandwiputra.skripsimentee.Utility.Constant;

import okhttp3.RequestBody;

/**
 * Created by alvinoktavianus on 6/5/17.
 */

public class Token {

    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static Token parseJSON(String JSON) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(JSON, Token.class);
    }

    public static RequestBody createJSONRequest(Token token) {
        Gson gson = new GsonBuilder().create();
        return RequestBody.create(Constant.MEDIA_TYPE_MARKDOWN, gson.toJson(token));
    }

    public static String createJSON(Token token) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(token);
    }
}
