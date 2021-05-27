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

import com.example.thumbup.DataBase.DBCallBack;
import com.example.thumbup.DataBase.DBManager;
import com.example.thumbup.DataBase.Schedule;

import java.util.Calendar;

import static com.example.thumbup.R.drawable.border_round_gray;
import static com.example.thumbup.R.drawable.schedule_error_box;

public class MeetingScheduleDialog extends Dialog {
    public MeetingScheduleDialog(@NonNull Context context) {
        super(context);
    }
    TextView dateNull, timeNull, placeNull;
    int dateState, timeState, placeState = 0; //미정 선택하지 않았을 경우
    TextView scheduleDate, scheduleYear, scheduleMonth, scheduleMap;
    EditText scheduleDay;
    TextView scheduleTime,schedule_ampm, scheduleHour, scheduleMinute;
    EditText scheduleName;
    int mYear, mMonth, mDay;
    Button dialogSave;
    Button dialogBack;
    DBManager dbManager = DBManager.getInstance();
    String dbScheduleName;
    String dbDate, dbMonth, dbDay;
    String dbTime, dbMinute;
    String dbAmPm = "오후";
    int dbHour;

    void updateDate(){
        scheduleDate.setText(String.format("%d년 %d월 %d일", mYear, mMonth + 1, mDay));
    }

    void popup_month() {
        PopupMenu monthPopup = new PopupMenu(getContext(), scheduleMonth);
        monthPopup.getMenuInflater().inflate(R.menu.schedule_hour, monthPopup.getMenu());
        monthPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dbMonth = item.getTitle().toString();
                scheduleMonth.setText(dbMonth);
                return false;
            }
        });
        monthPopup.show();
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
        ampmPopup.show();
    }

    void popup_hour() {
        PopupMenu hourPopup = new PopupMenu(getContext(), scheduleHour);
        hourPopup.getMenuInflater().inflate(R.menu.schedule_hour, hourPopup.getMenu());
        hourPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dbHour = Integer.parseInt(item.getTitle().toString());
                scheduleHour.setText(item.getTitle().toString());
                if (dbAmPm == "오후" && dbHour != 12) {
                    dbHour = dbHour + 12;
                } else if (dbAmPm == "오전" && dbHour == 12) {
                    dbHour = -12;
                }
                return false;
            }
        });
        hourPopup.show();
    }

    void popup_minute() {
        PopupMenu minutePopup = new PopupMenu(getContext(), scheduleMinute);
        minutePopup.getMenuInflater().inflate(R.menu.schedule_minute, minutePopup.getMenu());
        minutePopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dbMinute = item.getTitle().toString();
                scheduleMinute.setText(dbMinute);
                return false;
            }
        });
        minutePopup.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_meeting_schedule);
        dialogSave = (Button) findViewById(R.id.dialog_meeting_notice_save);
        dialogBack = (Button) findViewById(R.id.dialog_meeting_notice_back);
        dateNull = (TextView) findViewById(R.id.dateNull);
        timeNull = (TextView) findViewById(R.id.timeNull);
        scheduleName = (EditText) findViewById(R.id.schedule_name);
        //날짜관련
        scheduleYear = (TextView) findViewById(R.id.schedule_year);
        scheduleMonth = (TextView) findViewById(R.id.schedule_month);
        scheduleDay = (EditText) findViewById(R.id.schedule_day);
        //시간관련
        schedule_ampm = (TextView) findViewById(R.id.schedule_ampm);
        scheduleHour = (TextView) findViewById(R.id.schedule_hour);
        scheduleMinute = (TextView) findViewById(R.id.schedule_minute);

        //년도는 현재년도로
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        scheduleYear.setText(String.valueOf(mYear));
        //날짜 미정 선택시
        dateNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateState == 0) { //미정을 선택했을 경우
                    dateState = 1;
                    dateNull.setTextColor(Color.parseColor("#303F80"));
                    //날짜 초기화
                    scheduleYear.setText(""); scheduleYear.setBackgroundResource(R.drawable.schedule_disabled_box);
                    scheduleMonth.setText(""); scheduleMonth.setBackgroundResource(R.drawable.schedule_disabled_box);
                    scheduleDay.setText(""); scheduleDay.setBackgroundResource(R.drawable.schedule_disabled_box);
                    scheduleDay.setEnabled(false);
                    //db에 들어갈 데이터 설정
                    dbDate = "미정";
                    dbMonth = null;
                    dbDay = null;
                } else {
                    scheduleYear.setText(String.valueOf(mYear));
                    dateState = 0; scheduleYear.setBackgroundResource(border_round_gray);
                    scheduleMonth.setBackgroundResource(border_round_gray);
                    scheduleDay.setBackgroundResource(border_round_gray);
                    scheduleDay.setEnabled(true);
                    dateNull.setTextColor(Color.parseColor("#898A8D"));
                }
            }
        });
        timeNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeState == 0) { //미정을 선택했을 경우
                    timeState = 1;
                    timeNull.setTextColor(Color.parseColor("#303F80"));
                    //시간 초기화
                    schedule_ampm.setText(""); schedule_ampm.setBackgroundResource(R.drawable.schedule_disabled_box);
                    scheduleHour.setText(""); scheduleHour.setBackgroundResource(R.drawable.schedule_disabled_box);
                    scheduleMinute.setText(""); scheduleMinute.setBackgroundResource(R.drawable.schedule_disabled_box);
                    dbTime = "미정";
                    dbMinute = null;
                } else {
                    timeState = 0;
                    timeNull.setTextColor(Color.parseColor("#898A8D"));
                    dbAmPm = "오후";
                    schedule_ampm.setText(dbAmPm); schedule_ampm.setBackgroundResource(border_round_gray);
                    scheduleHour.setText(""); scheduleHour.setBackgroundResource(border_round_gray);
                    scheduleMinute.setText(""); scheduleMinute.setBackgroundResource(border_round_gray);
                }
            }
        });

        //달(month) 설정
        scheduleMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateState == 0) {
                    popup_month();
                }
            }
        });

        //오전, 오후 설정
        schedule_ampm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeState == 0) {
                    popup_ampm();
                }
            }
        });
        //시(hour) 설정
        scheduleHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeState == 0) {
                    popup_hour();
                }
            }
        });
        //분(minute) 설정
        scheduleMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeState == 0) {
                    popup_minute();
                }
            }
        });

        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("name", "일정제목: " + scheduleName.getText().toString());
                if (scheduleName.length() == 0) { //일정제목이 비어있을 때
                    scheduleName.setBackgroundResource(schedule_error_box);
                    scheduleName.setHint("일정 제목을 입력해주세요.");
                }else { // 일정제목이 쓰여있을 때
                    dbScheduleName = scheduleName.getText().toString(); //일정 이름
                    scheduleName.setBackgroundResource(border_round_gray);
                    dbDay = scheduleDay.getText().toString(); //날짜_일(day)
                    //날짜
                    if (dbMonth == null || dbDay == null) { //월 / 일 설정하지 않으면 날짜는 미정으로
                        dbDate = "미정";
                    } else {
                        dbDate = dbMonth + " / " + dbDay;
                    }

                    //시간
                    if (dbHour == 0 || dbMinute == null) { //시 분 설정하지 않으면 시간은 미정으로
                        dbTime = "미정";
                    }else if (dbHour == -12) { // 오전 12시일 때
                        dbTime = "00 : " + dbMinute;
                    } else {
                        dbTime = dbHour + " : " + dbMinute;
                    }
                    Log.d("date", "날짜는 " + dbDate);
                    Log.d("time", "시간은 " + dbTime);

                    Schedule schedule = new Schedule();
                    schedule.date = dbDate;
                    schedule.time = dbTime;
                    schedule.title = dbScheduleName;
                    schedule.place = "미정";
                    dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.add(schedule);
                    Log.e("schedule - ", "update before");
                    dbManager.UpdateMeeting("-MaZIcU6ZjxsYF_iX-6k", new DBCallBack() {
                        @Override
                        public void success(Object data) {
                            dismiss();
                            Log.e("schedule - ", "success");
                        }

                        @Override
                        public void fail(String errorMessage) {
                            dismiss();
                            Log.e("schedule - ", "fail");
                        }
                    });
                }
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
