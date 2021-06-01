package com.example.thumbup;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.view.View.OnClickListener;

public class MoneyAddFragment extends Fragment {
    public static MoneyAddFragment moneyAddContext;

    TextView money_add_mDate;
    Button price_sum;
    ImageView money_add_calBtn, addbtn1, addbtn2, addbtn3, addbtn4;
    int mYear, mMonth, mDay;
    LinearLayout menuBox1, menuBox2, menuBox3, menuBox4, menuBox5;

    EditText number1;
    EditText number2;
    EditText number3;
    EditText number4;
    EditText number5;
    TextView result;

    void updateDate(){
        money_add_mDate.setText(String.format("%d년 %d월 %d일", mYear, mMonth + 1, mDay));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View MoneyAddView = inflater.inflate(R.layout.activity_money_add, container, false);
        super.onCreate(savedInstanceState);

        moneyAddContext = this;
        money_add_mDate = (TextView) MoneyAddView.findViewById(R.id.meetingDate2);
        money_add_calBtn = (ImageView) MoneyAddView.findViewById(R.id.calendarBtn);
        menuBox1 = (LinearLayout) MoneyAddView.findViewById(R.id.menuBox1);
        menuBox2 = (LinearLayout) MoneyAddView.findViewById(R.id.menuBox2);
        menuBox3 = (LinearLayout) MoneyAddView.findViewById(R.id.menuBox3);
        menuBox4 = (LinearLayout) MoneyAddView.findViewById(R.id.menuBox4);
        menuBox5 = (LinearLayout) MoneyAddView.findViewById(R.id.menuBox5);

        price_sum = (Button) MoneyAddView.findViewById(R.id.meetingPriceSum);
        addbtn1 = (ImageView) MoneyAddView.findViewById(R.id.addBtn1);
        addbtn2 = (ImageView) MoneyAddView.findViewById(R.id.addBtn2);
        addbtn3 = (ImageView) MoneyAddView.findViewById(R.id.addBtn3);
        addbtn4 = (ImageView) MoneyAddView.findViewById(R.id.addBtn4);

        number1 = (EditText) MoneyAddView.findViewById(R.id.placeMenuPriceInput1);
        number2 = (EditText) MoneyAddView.findViewById(R.id.placeMenuPriceInput2);
        number3 = (EditText) MoneyAddView.findViewById(R.id.placeMenuPriceInput3);
        number4 = (EditText) MoneyAddView.findViewById(R.id.placeMenuPriceInput4);
        number5 = (EditText) MoneyAddView.findViewById(R.id.placeMenuPriceInput5);
        result = (TextView) MoneyAddView.findViewById(R.id.meetingPriceSum2);

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

        money_add_calBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MoneyAddView.getContext(), mDateSetListener, mYear,
                        mMonth, mDay).show();
            }
        });

        //덧셈 버튼 클릭시
        price_sum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int n1 = Integer.parseInt(number1.getText().toString());
                int n2 = Integer.parseInt(number2.getText().toString());
                int n3 = Integer.parseInt(number3.getText().toString());
                int n4 = Integer.parseInt(number4.getText().toString());
                int n5 = Integer.parseInt(number5.getText().toString());

                result.setText(Integer.toString(n1+n2+n3+n4+n5));
            }
        });

        addbtn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuBox2.setVisibility(View.VISIBLE);

            }
        });
        addbtn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuBox3.setVisibility(View.VISIBLE) ;

            }
        });
        addbtn3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuBox4.setVisibility(View.VISIBLE) ;

            }
        });
        addbtn4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuBox5.setVisibility(View.VISIBLE) ;
            }
        });

        return MoneyAddView;
    }
}

