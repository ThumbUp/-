package com.example.thumbup.DataBase;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    public String title;
    public String date;
    public String time;
    public String place;
    public List<String> members = new ArrayList<>();

    public Schedule() {
        title = "";
        date = "";
        time = "";
        place = "";
    }
}
