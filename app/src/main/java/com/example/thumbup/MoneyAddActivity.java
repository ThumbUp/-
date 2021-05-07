package com.example.thumbup;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

public class MoneyAddActivity extends AppCompatActivity {

    TextView money_add_mDate;
    ImageView money_add_calBtn;
    int mYear, mMonth, mDay;

    void updateDate(){
        money_add_mDate.setText(String.format("%d년 %d월 %d일", mYear, mMonth + 1, mDay));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_add);

        money_add_mDate = (TextView) findViewById(R.id.meetingDate2);
        money_add_calBtn = (ImageView) findViewById(R.id.calendarBtn);

        //현재 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언
        Calendar money_add_calendar = new GregorianCalendar();
        mYear = money_add_calendar.get(Calendar.YEAR);
        mMonth = money_add_calendar.get(Calendar.MONTH);
        mDay = money_add_calendar.get(Calendar.DATE);

        updateDate();

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

    }
}