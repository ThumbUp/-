package com.example.thumbup;

import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_ItemData extends AppCompatActivity {
    private Drawable data_meetingIcon;
    private String data_meetingTitle;
    private String data_meetingInfo;
    private String data_meetingId;

    public void setData_meetingIcon(Drawable icon) {
        data_meetingIcon = icon;
    }
    public void setData_meetingTitle(String title) {
        data_meetingTitle = title;
    }
    public void setData_meetingInfo(String info) { data_meetingInfo = info; }
    public void setData_meetingId(String mid) { data_meetingId = mid; }

    public Drawable getData_meetingIcon() { return this.data_meetingIcon; }
    public String getData_meetingTitle() {
        return this.data_meetingTitle;
    }
    public String getData_meetingInfo() {
        return this.data_meetingInfo;
    }
    public String getData_meetingId() {
        return this.data_meetingId;
    }
}