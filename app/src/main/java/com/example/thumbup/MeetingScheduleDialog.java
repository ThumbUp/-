package com.example.thumbup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.thumbup.DataBase.DBManager;

import java.util.Calendar;

public class MeetingScheduleDialog extends Dialog {
    public MeetingScheduleDialog(@NonNull Context context) {
        super(context);
    }
    TextView scheduleDate, scheduleTime, scheduleMap;
    ImageButton scheduleDateBtn;
    ImageButton scheduleTimeBtn;
    ImageButton scheduleMapBtn;
    int mYear, mMonth, mDay;
    Button dialogSave;
    Button dialogBack;
    DBManager dbManager = DBManager.getInstance();

    void updateDate(){
        scheduleDate.setText(String.format("%d년 %d월 %d일", mYear, mMonth + 1, mDay));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_meeting_schedule);
        scheduleDateBtn = (ImageButton) findViewById(R.id.ic_schedule_date);
        scheduleTimeBtn = (ImageButton) findViewById(R.id.ic_schedule_time);
        scheduleMapBtn = (ImageButton) findViewById(R.id.ic_schedule_map);
        dialogSave = (Button) findViewById(R.id.dialog_meeting_notice_save);
        dialogBack = (Button) findViewById(R.id.dialog_meeting_notice_back);

        //오늘의 날짜와 시간을 가져옴
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePicker = new DatePickerDialog(getOwnerActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                updateDate();
            }
        }, mYear, mMonth, mDay);

        DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        //사용자가 입력한 값을 가져옴
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;

                        updateDate();
                    }
                };

//        scheduleDateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog(this, mDateSetListener, mYear,
//                        mMonth, mDay).show(););
//            }
//        });

        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
