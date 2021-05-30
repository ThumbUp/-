package com.example.thumbup;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
//안녕하세요
//데이터베이스쪽은 정리했습니답
//세윤님 편하게 그냥 dbManager.Withraw하는 함수 만들었어요
//그리고 생각해보니 틀린 모임코드를 넣었을때 체크하는 부분이 없어서
//그부분 함수도 새로 만들엇습니답.
//감사합니다! Withdraw 사용해서 바로 키값 불러오면 되는 건가요?!
public class HomeActivity extends AppCompatActivity {
    ImageButton main_mainBtn;
    ImageButton main_meetingBtn;
    ImageButton main_moneyBtn;
    ImageButton main_loginBtn;
    private final int mainFragment1 = 1;
    private final int meetingFragment2 = 2;
    private final int moneyFragment3 = 3;
    private final int loginFragment4 = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_mainBtn = (ImageButton) findViewById(R.id.main_mainBtn);
        main_meetingBtn = (ImageButton) findViewById(R.id.main_meetingBtn);
        main_moneyBtn = (ImageButton) findViewById(R.id.main_moneyBtn);
        main_loginBtn = (ImageButton) findViewById(R.id.main_loginBtn);
        //기본 탭 : main
        FragmentView(mainFragment1);
        chooseTab(mainFragment1);

        //main 탭 눌렀을 때
        main_mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(mainFragment1);
                chooseTab(mainFragment1);
            }
        });
        //meeting 탭 눌렀을 때
        main_meetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(meetingFragment2);
                chooseTab(meetingFragment2);
            }
        });
        //money 탭 눌렀을 때
        main_moneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(moneyFragment3);
                chooseTab(moneyFragment3);
            }
        });
        //mypage 탭 눌렀을 때
        main_loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentView(loginFragment4);
                chooseTab(loginFragment4);
            }
        });
    }

    private void FragmentView(int fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment) {
            case 1:
                MainFragment mainFragment = new MainFragment();
                transaction.replace(R.id.main_frame, mainFragment);
                transaction.commit();
                break;
            case 2:
                MeetingFragment meetingFragment = new MeetingFragment();
                transaction.replace(R.id.main_frame, meetingFragment);
                transaction.commit();
                break;
            case 3:
                MoneyAddFragment moneyaddFragment = new MoneyAddFragment();
                transaction.replace(R.id.main_frame, moneyaddFragment);
                transaction.commit();
                break;
            case 4:
                AfterLoginFragment afterLoginFragment = new AfterLoginFragment();
                transaction.replace(R.id.main_frame, afterLoginFragment);
                transaction.commit();
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        transaction.commit();
    }

    private void chooseTab(int fragment) {
        main_mainBtn.setSelected(false);
        main_meetingBtn.setSelected(false);
        main_moneyBtn.setSelected(false);
        main_loginBtn.setSelected(false);
        switch (fragment) {
            case 1:
                main_mainBtn.setSelected(true);
                break;
            case 2:
                main_meetingBtn.setSelected(true);
                break;
            case 3:
                main_moneyBtn.setSelected(true);
                break;
            case 4:
                main_loginBtn.setSelected(true);
                break;
        }
    }
}
