package com.example.thumbup;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
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
//        findViewById(R.id.btnPopup).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
//                getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        if (menuItem.getItemId() == R.id.action_menu1){
//                            Toast.makeText(MainActivity.this, "메뉴 1 클릭", Toast.LENGTH_SHORT).show();
//                        }else if (menuItem.getItemId() == R.id.action_menu2){
//                            Toast.makeText(MainActivity.this, "메뉴 2 클릭", Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(MainActivity.this, "메뉴 3 클릭", Toast.LENGTH_SHORT).show();
//                        }
//
//                        return false;
//                    }
//                });
//                popupMenu.show();
//            }
//        });
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
                MoneyFragment moneyFragment = new MoneyFragment();
                transaction.replace(R.id.main_frame, moneyFragment);
                transaction.commit();
                break;
            case 4:
                LoginFragment loginFragment = new LoginFragment();
                transaction.replace(R.id.main_frame, loginFragment);
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