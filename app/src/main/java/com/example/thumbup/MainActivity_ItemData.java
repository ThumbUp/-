package com.example.thumbup;

import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_ItemData extends AppCompatActivity {

    public Drawable data_meetingIcon;
    public String data_meetingTitle;
    public String data_meetingInfo;

    public void setData_meetingIcon(Drawable icon) {
        data_meetingIcon = icon;
    }
    public void setData_meetingTitle(String title) {
        data_meetingTitle = title;
    }
    public void setData_meetingInfo(String info) {
        data_meetingInfo = info;
    }

    public Drawable getData_meetingIcon() {
        return this.data_meetingIcon;
    }
    public String getData_meetingTitle() {
        return this.data_meetingTitle;
    }
    public String getData_meetingInfo() {
        return this.data_meetingInfo;
    }
}