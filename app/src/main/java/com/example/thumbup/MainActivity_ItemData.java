package com.example.thumbup;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thumbup.DataBase.DBManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;

public class MainActivity_ItemData extends AppCompatActivity {
    private Drawable data_meetingIcon;
    private String data_meetingTitle;
    private String data_meetingInfo;
    private String data_meetingId;
    DBManager dbManager = DBManager.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public void setData_meetingIcon(Drawable icon) {
        data_meetingIcon = icon;
    }

    public void setData_meetingTitle(String title) {
        data_meetingTitle = title;
    }
    public void setData_meetingInfo(String info) {
        data_meetingInfo = info;
    }
    public void setData_meetingId(String mid) {
        data_meetingId = mid;
    }

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

    // 스트링을 바이너리 바이트 배열로
    public static byte[] binaryStringToByteArray(String s) {
        int count = s.length() / 8;
        byte[] b = new byte[count];
        for (int i = 1; i < count; ++i) {
            String t = s.substring((i - 1) * 8, i * 8);
            b[i - 1] = binaryStringToByte(t);
        }
        return b;
    }

    // 스트링을 바이너리 바이트로
    public static byte binaryStringToByte(String s) {
        byte ret = 0, total = 0;
        for (int i = 0; i < 8; ++i) {
            ret = (s.charAt(7 - i) == '1') ? (byte) (1 << i) : 0;
            total = (byte) (ret | total);
        }
        return total;
    }
}