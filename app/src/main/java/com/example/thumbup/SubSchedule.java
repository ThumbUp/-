package com.example.thumbup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thumbup.DataBase.DBCallBack;
import com.example.thumbup.DataBase.DBManager;
import com.example.thumbup.DataBase.Meeting;
import com.example.thumbup.DataBase.Schedule;
import com.example.thumbup.DataBase.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class SubSchedule extends AppCompatActivity {

    TextView title; //일정명
    TextView date; //날짜
    TextView time; //시간
    TextView personal;

    ImageView back_Btn;
    Button satrtLoc_Btn;
    TextView my_roc;
    Button re_cafe_btn;
    Button re_res_btn;
    Switch switchView;
    TextView re_map_text;

    DBManager dbManager = DBManager.getInstance();

    int clickedIndex;
    String myKey;
    int myKey2;
    int mykey;

    boolean meetingIn = false;

    List<Schedule> schedule = new ArrayList<>(); //일정

    String DBtitle; //DB 일정명
    String DBdate; //날짜
    String DBtime; //시간
    int DBpersonal; //참가인원수

    String roc; //설정 위치
    double roc_lati, roc_longi; //해당 위치의 위도와 경도 저장

    Context context;

    private static final String SWITCH_PARTIDOS_STATE = "switchPartidosState";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_schedule);

        //DB에서 모임명 가져올 것
        context = this;
        schedule = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules; //선택 일정 DB

        Intent outIntent = getIntent();
        String index = outIntent.getStringExtra("ListID");
        clickedIndex = Integer.parseInt(index); //선택 일정 인덱스

        DBtitle = schedule.get(clickedIndex).title;
        title = findViewById(R.id.meet_name2);
        title.setText(DBtitle); //일정명 변경

        DBdate = schedule.get(clickedIndex).date;
        date = findViewById(R.id.meet_day2);
        date.setText(DBdate); //날짜 변경

        DBtime = schedule.get(clickedIndex).time;
        time = findViewById(R.id.meet_time2);
        time.setText(DBtime); //시간 변경

        //dbManager.UpdateMeeting("-MaZIcU6ZjxsYF_iX-6k");
        DBpersonal = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(clickedIndex).members.size(); //int type
        personal = findViewById(R.id.meet_personnel2); //textView
        Log.e("PERSON", DBpersonal+"");
        personal.setText(DBpersonal+"");


        back_Btn = findViewById(R.id.backBtn);
        satrtLoc_Btn = findViewById(R.id.my_btn1);
        my_roc = findViewById(R.id.my_roc2);
        re_cafe_btn = findViewById(R.id.re_cafe_btn);
        re_res_btn = findViewById(R.id.re_res_btn);
        switchView = findViewById(R.id.switch1);
        re_map_text = findViewById(R.id.re_map_text);

        //sharedPreferences = getSharedPreferences("mySwichMode", Context.MODE_PRIVATE);
        //switchView.setChecked(sharedPreferences.getBoolean(SWITCH_PARTIDOS_STATE, false));

        roc = my_roc.getText().toString();
        re_map_text.setText("'" + roc + "' 근처 추천 지도 보기");

        DatabaseReference mdb;
        mdb = dbManager.returnMDB();

        //내 유저 정보 가져오기
        User my = dbManager.userData;
        String name = my.name;
        Log.e("MY DATA | ", name);

        // 일정 멤버에 내가 있으면 스위치 ON, 없으면 OFF
        DatabaseReference databaseReference =
                mdb.child("Meetings").child("-MaZIcU6ZjxsYF_iX-6k").child("schedules").child(clickedIndex+"").child("members");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String myKey_sw = postSnapshot.getKey();
                    int myKey2_sw = Integer.parseInt(myKey_sw);
                    List<User> users = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(clickedIndex).members;
                    // 예외인덱스 오류 발생 지점
                    if(users.get(myKey2_sw).email.equals(my.email) == true) {
                        switchView.setChecked(true);
                    }
                    else{
                        switchView.setChecked(false);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // 참여 유무 스위치 체인지 시, 변화
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //sharedPreferences.edit().putBoolean(SWITCH_PARTIDOS_STATE, isChecked).commit();
                // 스위치 ON이면 할 일
                if (isChecked) {
                    boolean meetingIn = false;
                    List<User> users = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(clickedIndex).members;
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).email.equals(my.email) == true) {
                            meetingIn = true;
                        }
                    }
                    if(meetingIn == false){
                        dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(clickedIndex).members.add(my);
                    }
                    // 로딩 Wait
                    dbManager.Lock(context);
                    dbManager.UpdateMeeting("-MaZIcU6ZjxsYF_iX-6k", new DBCallBack() {
                        @Override
                        public void success(Object data) {
                            dbManager.UnLock();
                            Log.e("PERSON ADD SIZE",
                                    dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(clickedIndex).members.size() + "");
                            personal.setText(dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(clickedIndex).members.size()+"");
                        }
                        @Override
                        public void fail(String errorMessage) {

                        }
                    });
                    // 일정-멤버 안에 나의 키 정보
                    DatabaseReference databaseReference =
                            mdb.child("Meetings").child("-MaZIcU6ZjxsYF_iX-6k").child("schedules").child(clickedIndex+"").child("members");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                myKey = postSnapshot.getKey();
                                myKey2 = Integer.parseInt(myKey);
                                List<User> users = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(clickedIndex).members;
                                if(users.get(myKey2).email.equals(my.email) == true) {
                                    mykey = myKey2;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    // 시작설정버튼 ON
                    satrtLoc_Btn.setText("시작 위치 설정하기");
                    satrtLoc_Btn.setEnabled(true);
                }
                // 스위치 OFF면 할 일
                else {
                    List<User> users = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(clickedIndex).members;
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).email.equals(my.email) == true) {
                            users.remove(i);
                            break;
                        }
                    }
                    // 로딩 Wait
                    dbManager.Lock(context);
                    dbManager.UpdateMeeting("-MaZIcU6ZjxsYF_iX-6k", new DBCallBack() {
                        @Override
                        public void success(Object data) {
                            dbManager.UnLock();
                            Log.e("PERSON REMOVE SIZE",
                                    dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(clickedIndex).members.size() + "");
                            personal.setText(dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(clickedIndex).members.size()+"");

                        }
                        @Override
                        public void fail(String errorMessage) {

                        }
                    });
                    // 시작설정버튼 OFF
                    satrtLoc_Btn.setText("일정 미참여 시, 시작 위치를 설정할 수 없습니다");
                    satrtLoc_Btn.setEnabled(false);
                }
            }
        });

        // 뒤로가기 버튼 클릭
        back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 시작 위치 설정 버튼 클릭
        satrtLoc_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StartLocationActivity.class);
                intent.putExtra("Rocation", roc);
                intent.putExtra("MyKey", mykey);
                intent.putExtra("ScheduleIndex", clickedIndex);
                startActivityForResult(intent, 0);
            }
        });

        // 카페 추천 지도 이동
        re_cafe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecommendPlaceActivity.class);
                intent.putExtra("Rocation", roc);
                startActivityForResult(intent, 1);
            }
        });

        // 식당 추천 지도 이동
        re_res_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecommendPlace2Activity.class);
                intent.putExtra("Rocation", roc);
                startActivityForResult(intent, 2);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            roc = data.getStringExtra("Place");
            roc_lati = data.getDoubleExtra("Latitude", 0);
            roc_longi = data.getDoubleExtra("Longitude", 0);

            my_roc.setText(roc);
            re_map_text.setText("'" + roc + "' 근처 추천 지도 보기");
        }
    }

}