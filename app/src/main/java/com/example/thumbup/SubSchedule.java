package com.example.thumbup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class SubSchedule extends AppCompatActivity {
    //String meetingId = getIntent().getStringExtra("meetingId"); //선택된 모임의 아이디(=코드)
    TextView title; //일정명
    TextView date; //날짜
    TextView time; //시간
    TextView personal;

    RelativeLayout person;

    ImageView back_Btn;
    ImageView delete_Btn; //일정 삭제 버튼
    Button satrtLoc_Btn;
    TextView my_roc;
    Button re_cafe_btn;
    Button re_res_btn;
    Switch switchView;

    DBManager dbManager = DBManager.getInstance();

    int clickedIndex_sche;
    String myKey;
    int myKey2;
    int mykey;

    List<Schedule> schedule = new ArrayList<>(); //일정

    TextView name;
    String DBmeetTitle;
    String DBtitle; //DB 일정명
    String DBdate; //날짜
    String DBtime; //시간
    int DBpersonal; //참가인원수

    String roc; //설정 위치
    //double roc_lati, roc_longi; //해당 위치의 위도와 경도 저장

    Context context;

    private static final String SWITCH_PARTIDOS_STATE = "switchPartidosState";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_schedule);
        Intent outIntent = getIntent();
        String index_sche = outIntent.getStringExtra("ListID");
        clickedIndex_sche = Integer.parseInt(index_sche); //선택 일정 인덱스
        String clickedId_meet = outIntent.getStringExtra("MeetID"); //선택 미팅 아이디
        name = (TextView) findViewById(R.id.name);

        //DB에서 모임명 가져올 것
        context = this;
        schedule = dbManager.participatedMeetings.get(clickedId_meet).schedules; //선택 일정 DB

        DBmeetTitle = dbManager.participatedMeetings.get(clickedId_meet).title;

        DBtitle = schedule.get(clickedIndex_sche).title;
        title = findViewById(R.id.meet_name2);
        title.setText(DBtitle); //일정명 변경

        DBdate = schedule.get(clickedIndex_sche).date;
        date = findViewById(R.id.meet_day2);
        date.setText(DBdate); //날짜 변경

        DBtime = schedule.get(clickedIndex_sche).time;
        time = findViewById(R.id.meet_time2);
        time.setText(DBtime); //시간 변경
        delete_Btn = (ImageView) findViewById(R.id.deleteBtn);

        //dbManager.UpdateMeeting(meetingId);
        DBpersonal = dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.size(); //int type
        personal = findViewById(R.id.meet_personnel2); //textView
        Log.e("PERSON", DBpersonal+"");
        personal.setText(DBpersonal+"");

        person = findViewById(R.id.meet_personnel);

        back_Btn = findViewById(R.id.backBtn);
        satrtLoc_Btn = findViewById(R.id.my_btn1);
        my_roc = findViewById(R.id.my_roc2); //설정위치 텍스트
        re_cafe_btn = findViewById(R.id.re_cafe_btn);
        re_res_btn = findViewById(R.id.re_res_btn);
        switchView = findViewById(R.id.switch1);

        DatabaseReference mdb;
        mdb = dbManager.returnMDB();
        //모임 제목 세팅
        name.setText(DBmeetTitle);

        //내 유저 정보 가져오기
        User my = dbManager.userData;
        String name = my.name;
        Log.e("MY DATA | ", name);

        // 일정 멤버에 내가 있으면 스위치 ON, 없으면 OFF
        DatabaseReference databaseReference =
            mdb.child("Meetings").child(clickedId_meet+"").child("schedules").child(clickedIndex_sche+"").child("members");
        switchView.setChecked(false);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long si = dataSnapshot.getChildrenCount();
                Log.e("CHILD GET SIZE", si+"");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String myKey_sw = postSnapshot.getKey();
                    int myKey2_sw = Integer.parseInt(myKey_sw);
                    //long si = postSnapshot.getChildrenCount();
                    //Log.e("CHILD GET KEY", myKey_sw);

                    List<User> users = dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members;
                    // 예외인덱스 오류 발생 지점
                    if(users.get(myKey2_sw).email.equals(my.email) == true) {
                        switchView.setChecked(true);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //일정 삭제 시 나오는 대화 상자
        AlertDialog.Builder scheduleDeleteBuilder = new AlertDialog.Builder(SubSchedule.this);
        scheduleDeleteBuilder.setTitle("일정 삭제");
        scheduleDeleteBuilder.setMessage("'" + DBtitle + "'" +  " 일정이 " + DBmeetTitle + " 모임에서 영구 삭제됩니다. \n삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbManager.participatedMeetings.get(clickedId_meet).schedules.remove(clickedIndex_sche);
                        dbManager.UpdateMeeting(clickedId_meet);
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        //일정 삭제 버튼 클릭시
        delete_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog scheduleDeleteDialog = scheduleDeleteBuilder.create();
                scheduleDeleteDialog.show();
            }
        });
        // 참여 유무 스위치 체인지 시, 변화
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 ON이면 할 일
                if (isChecked) {
                    boolean meetingIn = false;
                    List<User> users = dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members;
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).email.equals(my.email) == true) {
                            meetingIn = true;
                        }
                    }
                    if(meetingIn == false){
                        Log.e("MY placeName ", my.placeName);

                        dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.add(my);

                    }
                    // 로딩 Wait
                    dbManager.Lock(context);
                    dbManager.UpdateMeeting(clickedId_meet, new DBCallBack() {
                        @Override
                        public void success(Object data) {
                            Log.e("PERSON ADD SIZE",
                                    dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.size() + "");
                            personal.setText(dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.size()+"");

                            // 일정-멤버 안에 나의 키 정보
                            DatabaseReference databaseReference =
                                    mdb.child("Meetings").child(clickedId_meet+"").child("schedules").child(clickedIndex_sche+"").child("members");
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        myKey = postSnapshot.getKey();
                                        myKey2 = Integer.parseInt(myKey);
                                        List<User> users = dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members;
                                        if(users.get(myKey2).email.equals(my.email) == true) {
                                            mykey = myKey2;

                                            roc = dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.get(mykey).placeName; //설정위치
                                            my_roc.setText(roc);
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

                            dbManager.UnLock();
                        }
                        @Override
                        public void fail(String errorMessage) {

                        }
                    });
                }
                // 스위치 OFF면 할 일
                else {
                    List<User> users = dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members;
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).email.equals(my.email) == true) {
                            users.remove(i);
                            break;
                        }
                    }
                    // 로딩 Wait
                    dbManager.Lock(context);
                    dbManager.UpdateMeeting(clickedId_meet, new DBCallBack() {
                        @Override
                        public void success(Object data) {
                            Log.e("PERSON REMOVE SIZE",
                                    dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.size() + "");
                            personal.setText(dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.size()+"");

                            my_roc.setText("");

                            // 시작설정버튼 OFF
                            satrtLoc_Btn.setText("일정 미참여 시, 시작 위치를 설정할 수 없습니다");
                            satrtLoc_Btn.setEnabled(false);

                            dbManager.UnLock();
                        }
                        @Override
                        public void fail(String errorMessage) {

                        }
                    });
                }
            }
        });

        // 참여인원 클릭
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu meetingPopup = new PopupMenu(getApplicationContext(), view);
                Menu meetingMenu = meetingPopup.getMenu();

                int person_size = dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.size();
                for (int i = 0; i < person_size; i++) {
                    meetingMenu.add(0, i,0,
                            (CharSequence) dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.get(i).name);

                }
                meetingPopup.show();
            }
        });

        // 뒤로가기 버튼 클릭
        back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbManager.meetingFrament.showSchedule();
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
                intent.putExtra("MeetingId", clickedId_meet);
                intent.putExtra("ScheduleIndex", clickedIndex_sche);
                startActivityForResult(intent, 0);
            }
        });

        // 카페 추천 지도 이동
        re_cafe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double lati_sum = 0;
                double longi_sum = 0;
                int DBpersonal1 = dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.size();
                for(int i=0; i<DBpersonal1; i++){
                    lati_sum += dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.get(i).latitude;
                    longi_sum += dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.get(i).longitude;

                }
                double lati_ev = lati_sum / (double)DBpersonal1;
                double longi_ev = longi_sum / (double)DBpersonal1;

                Intent intent = new Intent(getApplicationContext(), RecommendPlaceActivity.class);
                intent.putExtra("midLatitude", lati_ev);
                intent.putExtra("midLongitude", longi_ev);
                startActivity(intent);
            }
        });

        // 식당 추천 지도 이동
        re_res_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double lati_sum = 0;
                double longi_sum = 0;
                int DBpersonal1 = dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.size();
                for(int i=0; i<DBpersonal1; i++){
                    lati_sum += dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.get(i).latitude;
                    longi_sum += dbManager.participatedMeetings.get(clickedId_meet).schedules.get(clickedIndex_sche).members.get(i).longitude;

                }
                double lati_ev = lati_sum / (double)DBpersonal1;
                double longi_ev = longi_sum / (double)DBpersonal1;

                Intent intent = new Intent(getApplicationContext(), RecommendPlace2Activity.class);
                intent.putExtra("midLatitude", lati_ev);
                intent.putExtra("midLongitude", longi_ev);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            roc = data.getStringExtra("Place"); // 설정한 나의 위치
            String mId = data.getStringExtra("meetId");
            dbManager.participatedMeetings.get(mId).schedules.get(clickedIndex_sche).members.get(mykey).placeName = roc;

            dbManager.Lock(context);
            dbManager.UpdateMeeting(mId, new DBCallBack() {
                @Override
                public void success(Object data) {
                    my_roc.setText(roc);
                    dbManager.UnLock();
                }
                @Override
                public void fail(String errorMessage) {

                }
            });
        }
    }

}