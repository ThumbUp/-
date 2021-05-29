package com.example.thumbup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class SubSchedule extends AppCompatActivity {

    TextView title; //일정명
    TextView date; //날짜
    TextView time; //시간

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

    List<Schedule> schedule = new ArrayList<>(); //일정

    String DBtitle; //DB 일정명
    String DBdate; //날짜
    String DBtime; //시간

    String roc; //설정 위치
    double roc_lati, roc_longi; //해당 위치의 위도와 경도 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_schedule);

        //DB에서 모임명 가져올 것

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

        back_Btn = findViewById(R.id.backBtn);
        satrtLoc_Btn = findViewById(R.id.my_btn1);
        my_roc = findViewById(R.id.my_roc2);
        re_cafe_btn = findViewById(R.id.re_cafe_btn);
        re_res_btn = findViewById(R.id.re_res_btn);
        switchView = findViewById(R.id.switch1);
        re_map_text = findViewById(R.id.re_map_text);

        roc = my_roc.getText().toString();
        re_map_text.setText("'" + roc + "' 근처 추천 지도 보기");

        DatabaseReference mdb;
        mdb = dbManager.returnMDB();

        //내 유저 정보 가져오기
        User my = dbManager.userData;
        String name = my.name;
        Log.e("MY DATA | ", name);

        //COPY
        //schedule.get(clickedIndex).members.add(my);

        // 참여 유무 스위치 체인지
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //True이면 할 일
                    //schedule.get(clickedIndex).members.add(my); //배열, DB 추가

//                    DatabaseReference databaseReference =
//                            mdb.child("Meetings").child("-MaZIcU6ZjxsYF_iX-6k").child("schedules").child(clickedIndex+"").child("members");
//                    databaseReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                                myKey = postSnapshot.getKey();
//                                Log.e("KEY", myKey);
//                                //schedule.get(clickedIndex).members.add(my);
//                            }
//                        }
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
                    dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(clickedIndex).members.add(my);
                    dbManager.UpdateMeeting("-MaZIcU6ZjxsYF_iX-6k");
                    Log.e("SIZE", schedule.get(clickedIndex).members.size()+"");

                    satrtLoc_Btn.setText("시작 위치 설정하기");
                    satrtLoc_Btn.setEnabled(true);
                }
                else{
                    //False이면 할 일
                    List<User> users = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(clickedIndex).members;
                    for(int i=0; i<users.size(); i++)
                    {
                        if(users.get(i).email.equals(my.email) == true)
                        {
                            users.remove(i);
                            break;
                        }
                    }
                    dbManager.UpdateMeeting("-MaZIcU6ZjxsYF_iX-6k");
                    Log.e("SIZE", schedule.get(clickedIndex).members.size()+"");

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