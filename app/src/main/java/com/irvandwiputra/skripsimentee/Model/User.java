package com.irvandwiputra.skripsimentee.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.irvandwiputra.skripsimentee.Utility.Constant;

import okhttp3.RequestBody;

/**
 * Created by Irvan Dwi Putra on 6/1/2017.
 */

public class User {

    private String email;
    private String name;
    private String password;
    private String phone;
    private String address;
    private String role;
    private String occupation;

    public User() {
    }

    public User(String email, String name, String password, String phone, String address, String role, String occupation) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.occupation = occupation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public static RequestBody createJSONRequest(User user) {
        Gson gson = new Gson();
        return RequestBody.create(Constant.MEDIA_TYPE_MARKDOWN, gson.toJson(user));
    }

    public static String createJSON(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    public static User parseJSON(String JSON) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(JSON, User.class);
    }

}
