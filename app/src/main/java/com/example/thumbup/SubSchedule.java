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
        //안됬던 이유는 현제 DB에서 파이어베이스 내용이 바뀔때마다, 실시간으로 정보를 db매니저에 가져온단말이에요?네넵
        //그래서 participatedMeetings 내부 내용이 db가 업데이트 될때마다 새로운 변수로 바뀌는데,
        //위에 schedule = ~~ 이 코드는 처음 화면이 만들어질때만 실행되는 코드잖아요?네
        //그래서 처음 updateMeeting한후에 새롭게 미팅정보가 바뀌니까 자동으로 업데이트되서, 변수가 바뀌는데
        //위의 shcedule은 옛날 변수를 들고있는거죠 이해하셧나용 어.. 그럼 어떻게 수정하신거죠?!

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

        sharedPreferences = getSharedPreferences("mySwichMode", Context.MODE_PRIVATE);
        switchView.setChecked(sharedPreferences.getBoolean(SWITCH_PARTIDOS_STATE, false));

        roc = my_roc.getText().toString();
        re_map_text.setText("'" + roc + "' 근처 추천 지도 보기");

        DatabaseReference mdb;
        mdb = dbManager.returnMDB();

        //내 유저 정보 가져오기
        User my = dbManager.userData;
        String name = my.name;
        Log.e("MY DATA | ", name);

        // 참여 유무 스위치 체인지
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferences.edit().putBoolean(SWITCH_PARTIDOS_STATE, isChecked).commit();
                if (isChecked) {
                    //True이면 할 일
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

                    //dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(clickedIndex).members.add(my);
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

                    DatabaseReference databaseReference =
                            mdb.child("Meetings").child("-MaZIcU6ZjxsYF_iX-6k").child("schedules").child(clickedIndex+"").child("members");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                myKey = postSnapshot.getKey();
                                myKey2 = Integer.parseInt(myKey);
                                //Log.e("KEY", mykey+"");
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

                    satrtLoc_Btn.setText("시작 위치 설정하기");
                    satrtLoc_Btn.setEnabled(true);
                } else {
                    //False이면 할 일
                    //그리고 삭제부분에서, 원래 remove(my)이렇게 하셧잖아요?ㄴ
                    //저렇게쓰면 my와 같은 주소값을 가진 리스트 내부값이 삭제가되는데
                    //제가 아까 해당 변수를 다 새로가져온다고 했잖아요? 아네넨
                    //그래서 똑같은 주소를 가진my가 없으니 삭제가 안되는거죠 리스트에서
                    //그래서 그냥 email같은 리스트값이 잇으면 삭제하게 바꿧어요 네! 이해했씁니ㅏㄷ!
                    //굳굳 감사해요 ㅎ!!
                    //아니에요 그리고 updateMeeting에 콜백으로 작업해줘야 그
                    //그 버튼을 뭐라하죠! 오니쪽오른쪽 왓다갓다하는것!스위치요?>? 네네 스위치 마구마구 누르면
                    //오류날 가능성이 농후해서
                    List<User> users = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(clickedIndex).members;
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).email.equals(my.email) == true) {
                            users.remove(i);
                            break;
                        }
                    }

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
                    Log.e("SIZE", schedule.get(clickedIndex).members.size() + "");

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