package com.irvandwiputra.skripsimentee.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
}
