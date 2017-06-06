package com.irvandwiputra.skripsimentee.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by alvinoktavianus on 6/6/17.
 */

public class ResponseStatus {

    private String code;
    private String message;

    public ResponseStatus() {
    }

    public ResponseStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResponseStatus parseJSON(String JSON) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(JSON, ResponseStatus.class);
    }
}
