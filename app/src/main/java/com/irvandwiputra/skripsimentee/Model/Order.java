package com.irvandwiputra.skripsimentee.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.irvandwiputra.skripsimentee.Utility.Constant;

import okhttp3.RequestBody;

/**
 * Created by alvinoktavianus on 6/8/17.
 */

public class Order {

    private String token;
    public int id;
    private String order_no;
    private int total_price;
    private int course_id;
    private String order_description;
    private String mentee_id;
    private String mentor_id;
    private String status;
    private int duration;
    private int price;
    private double latitude;
    private double longitude;

    public Order() {
    }

    public Order(String token, int id, String order_no, int total_price, int course_id, String order_description, String mentee_id, String mentor_id, String status, int duration, int price, double latitude, double longitude) {
        this.token = token;
        this.id = id;
        this.order_no = order_no;
        this.total_price = total_price;
        this.course_id = course_id;
        this.order_description = order_description;
        this.mentee_id = mentee_id;
        this.mentor_id = mentor_id;
        this.status = status;
        this.duration = duration;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getOrder_description() {
        return order_description;
    }

    public void setOrder_description(String order_description) {
        this.order_description = order_description;
    }

    public String getMentee_id() {
        return mentee_id;
    }

    public void setMentee_id(String mentee_id) {
        this.mentee_id = mentee_id;
    }

    public String getMentor_id() {
        return mentor_id;
    }

    public void setMentor_id(String mentor_id) {
        this.mentor_id = mentor_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Order parseJSON(String JSON) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(JSON, Order.class);
    }

    public static RequestBody createJSONRequest(Order order) {
        Gson gson = new Gson();
        return RequestBody.create(Constant.MEDIA_TYPE_MARKDOWN, gson.toJson(order));
    }

    public static String createJSON(Order order) {
        Gson gson = new Gson();
        return gson.toJson(order);
    }

}
