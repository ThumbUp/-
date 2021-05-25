package com.example.thumbup;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;

public class MoneyAddActivity extends AppCompatActivity {
    public static Context moneyAddContext;
    ImageView backBtn;
    TextView money_add_mDate;
    ImageView money_add_calBtn;
    int mYear, mMonth, mDay;
    LinearLayout menuBox;
    RelativeLayout addMenu;
    ImageButton showSchedule;
    TextView scheduleName;

    void updateDate(){
        money_add_mDate.setText(String.format("%d년 %d월 %d일", mYear, mMonth + 1, mDay));
    }
    public void showSchedule() {
        PopupMenu schedulePopup = new PopupMenu(getApplicationContext(), scheduleName);
        Menu scheduleMenu = schedulePopup.getMenu();
        scheduleMenu.add("일정1");
        scheduleMenu.add("일정2");
        schedulePopup.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_add);

        moneyAddContext = this;
        money_add_mDate = (TextView) findViewById(R.id.meetingDate2);
        money_add_calBtn = (ImageView) findViewById(R.id.calendarBtn);
        backBtn = (ImageView) findViewById(R.id.backBtn);
        menuBox = (LinearLayout) findViewById(R.id.menuBox);
        addMenu = (RelativeLayout) findViewById(R.id.addMenu);
        showSchedule = (ImageButton) findViewById(R.id.showSchedule);
        scheduleName = (TextView) findViewById(R.id.schedule_name);

        //이전 버튼 클릭시 액티비티 종료
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        showSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSchedule();
            }
        });

        //현재 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언
        Calendar money_add_calendar = new GregorianCalendar();
        mYear = money_add_calendar.get(Calendar.YEAR);
        mMonth = money_add_calendar.get(Calendar.MONTH);
        mDay = money_add_calendar.get(Calendar.DAY_OF_MONTH);

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

        money_add_calBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MoneyAddActivity.this, mDateSetListener, mYear,
                        mMonth, mDay).show();
            }
        });

        //메뉴 추가시 메뉴, 가격 추가
        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater menuInflater = (LayoutInflater) getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
                menuInflater.inflate(R.layout.inflater_money_add_menu, menuBox, true);
            }
        });
    }
}