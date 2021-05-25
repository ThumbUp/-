package com.example.thumbup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.thumbup.DataBase.DBManager;

import java.util.Calendar;

public class MeetingScheduleDialog extends Dialog {
    public MeetingScheduleDialog(@NonNull Context context) {
        super(context);
    }
    TextView dateNull, timeNull, placeNull;
    TextView schedule_ampm; //오전인지 오후인지
    int dateState, timeState, placeState = 0; //미정 선택하지 않았을 경우
    TextView scheduleDate, scheduleTime, scheduleMap;
    EditText scheduleName;
    ImageButton scheduleDateBtn;
    ImageButton scheduleTimeBtn;
    ImageButton scheduleMapBtn;
    int mYear, mMonth, mDay;
    Button dialogSave;
    Button dialogBack;
    DBManager dbManager = DBManager.getInstance();
    String dbDate, dbScheduleName, dbPlace;
    String dbTime, dbAmPm, dbHour, dbMonth;

    void updateDate(){
        scheduleDate.setText(String.format("%d년 %d월 %d일", mYear, mMonth + 1, mDay));
    }

    void popup_ampm() {
        PopupMenu ampmPopup = new PopupMenu(getContext(), schedule_ampm);
        Menu ampmMenu = ampmPopup.getMenu();
        ampmMenu.add("오전");
        ampmMenu.add("오후");
        ampmPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dbAmPm = item.getTitle().toString();
                schedule_ampm.setText(dbAmPm);
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_meeting_schedule);
        scheduleMapBtn = (ImageButton) findViewById(R.id.ic_schedule_map);
        dialogSave = (Button) findViewById(R.id.dialog_meeting_notice_save);
        dialogBack = (Button) findViewById(R.id.dialog_meeting_notice_back);
        dateNull = (TextView) findViewById(R.id.dateNull);
        timeNull = (TextView) findViewById(R.id.timeNull);
        placeNull = (TextView) findViewById(R.id.placeNull);
        scheduleName = (EditText) findViewById(R.id.schedule_name);
        schedule_ampm = (TextView) findViewById(R.id.schedule_ampm);
        //미정 선택시
        dateNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateState == 0) {
                    dateState = 1;
                    dateNull.setTextColor(Color.parseColor("#303F80"));
                    dbDate = "미정";
                } else {
                    dateState = 0;
                    dateNull.setTextColor(Color.parseColor("#898A8D"));
                    dbDate = "";
                }
            }
        });
        timeNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeState == 0) {
                    timeState = 1;
                    timeNull.setTextColor(Color.parseColor("#303F80"));
                    dbTime = "미정";
                } else {
                    timeState = 0;
                    timeNull.setTextColor(Color.parseColor("#898A8D"));
                    dbTime = "";
                }
            }
        });
        placeNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placeState == 0) {
                    placeState = 1;
                    placeNull.setTextColor(Color.parseColor("#303F80"));
                    dbPlace = "미정";
                } else {
                    placeState = 0;
                    placeNull.setTextColor(Color.parseColor("#898A8D"));
                    dbPlace = "";
                }
            }
        });

        //오늘의 날짜와 시간을 가져옴
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbScheduleName = scheduleName.getText().toString(); //일정 이름
                dismiss();
            }
        });

        dialogBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
