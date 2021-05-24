package com.example.thumbup;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MoneyAddFragment extends Fragment {
    TextView money_add_mDate;
    ImageView money_add_calBtn;
    int mYear, mMonth, mDay;
    public static MoneyFragment newInstance() {
        return new MoneyFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View moneyAddFragment = inflater.inflate(R.layout.activity_money_add, container, false);
        money_add_mDate = (TextView) moneyAddFragment.findViewById(R.id.meetingDate2);
        money_add_calBtn = (ImageView) moneyAddFragment.findViewById(R.id.calendarBtn);


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
                new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth, mDay).show();
            }
        });
        return moneyAddFragment;
    }

    void updateDate(){
        money_add_mDate.setText(String.format("%d년 %d월 %d일", mYear, mMonth + 1, mDay));
    }
}
