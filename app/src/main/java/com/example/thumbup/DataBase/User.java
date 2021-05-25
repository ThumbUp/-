package com.example.thumbup.DataBase;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String name;
    public String email;
    public String profile;
    public List<String> meetings = new ArrayList<>();

    public User()
    {

    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.profile = "";
    }
}
