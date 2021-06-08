package com.thumbUpB.thumbup.DataBase;

import java.util.ArrayList;
import java.util.List;


public class MoneyHistory {
    public String place;
    public List<String> members;
    public List<MyMenu> menus;

    public MoneyHistory() {
        place = "";
        members = new ArrayList<>();
        menus = new ArrayList<>();
    }
}
