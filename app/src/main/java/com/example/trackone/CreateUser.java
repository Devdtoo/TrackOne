package com.example.trackone;

public class CreateUser {

    public CreateUser() {

    }

    public String name;

    public CreateUser(String name, String email, String password,  String code, String isSharing, String date, String lat, String lng, String imageUrl, String userId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.date = date;
        this.isSharing = isSharing;
        this.code = code;
        this.lat = lat;
        this.lng = lng;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public String email;
    public String password;
    public String code;
    public String isSharing;
    public String date;
    public String lat;
    public String lng;
    public String imageUrl;
    public String userId;


}
