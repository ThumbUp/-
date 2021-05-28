package com.example.thumbup.DataBase;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String name;
    public String email;
    public String profile;
    public List<String> meetings = new ArrayList<>();
    public double latitude;
    public double longitude;
    public String placeName;

    public User()
    {

    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.profile = "";
        this.latitude = 0;
        this.longitude = 0;
        this.placeName = "";
    }

    public User(String name, String email, double latitude, double longitude, String placeName) {
        this.name = name;
        this.email = email;
        this.profile = "";
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }
}