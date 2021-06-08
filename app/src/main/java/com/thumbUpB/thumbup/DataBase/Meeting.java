package com.thumbUpB.thumbup.DataBase;

import java.util.ArrayList;
import java.util.List;

public class Meeting {
    public String title;
    public String info;
    public String image;
    public List<Schedule> schedules;
    public List<String> notices;
    public List<String> members;
    public List<MoneyHistory> moneyHistories;

    public Meeting() {
        title = "";
        info = "";
        image = "";
        notices = new ArrayList<>();
        schedules = new ArrayList<>();
        members = new ArrayList<>();
        moneyHistories = new ArrayList<>();
    }

    public Meeting(String title, String info, String image)
    {
        this.title = title;
        this.info = info;
        this.image = image;
        notices = new ArrayList<>();
        schedules = new ArrayList<>();
        members = new ArrayList<>();
        moneyHistories = new ArrayList<>();
    }
}
