package com.example.thumbup;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MoneyAddFragment extends Fragment {
    public static MoneyAddFragment moneyAddContext;
    TextView money_add_mDate, price_sum;
    ImageView money_add_calBtn;
    int mYear, mMonth, mDay;
    LinearLayout menuBox;
    RelativeLayout addMenu;
    EditText place, price;

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
        menuBox = (LinearLayout) MoneyAddView.findViewById(R.id.menuBox);
        addMenu = (RelativeLayout) MoneyAddView.findViewById(R.id.addMenu);
        place = (EditText) MoneyAddView.findViewById(R.id.placeInput);
        price = (EditText) MoneyAddView.findViewById(R.id.placeMenuPriceInput);
        price_sum = (TextView) MoneyAddView.findViewById(R.id.meetingPriceSum);

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
                new DatePickerDialog(MoneyAddView.getContext(), mDateSetListener, mYear,
                        mMonth, mDay).show();
            }
        });

        /*//메뉴 추가시 메뉴, 가격 추가
        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater menuInflater = (LayoutInflater) getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
                menuInflater.inflate(R.layout.inflater_money_add_menu, menuBox, true);
            }
        });*/


        //덧셈 버튼 클릭시

        price_sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText number1 = (EditText) MoneyAddView.findViewById(R.id.placeMenuPriceInput1);
                EditText number2 = (EditText) MoneyAddView.findViewById(R.id.placeMenuPriceInput2);
                EditText number3 = (EditText) MoneyAddView.findViewById(R.id.placeMenuPriceInput3);

                TextView result = (TextView) MoneyAddView.findViewById(R.id.meetingPriceSum2);

                int n1 = Integer.parseInt(number1.getText().toString());
                int n2 = Integer.parseInt(number2.getText().toString());
                int n3 = Integer.parseInt(number3.getText().toString());

                result.setText(Integer.toString(n1+n2+n3));
            }
        });

        return MoneyAddView;
    }
}

