package com.irvandwiputra.skripsimentee.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by alvinoktavianus on 6/9/17.
 */

public class Course {

    private int id;
    private String name;

    public Course() {
    }

    public Course(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Course parseJSON(String JSON) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(JSON, Course.class);
    }

    public static Course[] parseJSONArray(String JSON) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(JSON, Course[].class);
    }
}
