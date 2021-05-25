package com.example.thumbup.DataBase;

import java.util.ArrayList;
import java.util.List;

public class Meeting {
    public String title;
    public String summary;
    public String image;
    public List<Schedule> schedules;
    public List<String> notices;
    public List<String> members;
    public List<MoneyHistory> moneyHistories;

    public Meeting() {
        title = "";
        summary = "";
        image = "";
        notices = new ArrayList<>();
        schedules = new ArrayList<>();
        members = new ArrayList<>();
        moneyHistories = new ArrayList<>();
    }
}
