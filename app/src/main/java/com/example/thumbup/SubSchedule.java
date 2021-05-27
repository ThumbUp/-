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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SubSchedule extends AppCompatActivity {

    ImageView back_Btn;
    Button satrtLoc_Btn;
    TextView my_roc;
    Button re_cafe_btn;
    Button re_res_btn;
    Switch switchView;
    TextView re_map_text;
    DBManager dbManager = DBManager.getInstance();

    DBManager dbManager = DBManager.getInstance();

    int clickedIndex;

    String DBtitle; //일정명
    String DBdate; //날짜
    String DBtime; //시간

    String roc; //설정 위치
    double roc_lati, roc_longi; //해당 위치의 위도와 경도 저장

    void titleGetDB(){
        //DBtitle = dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").notices;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_schedule);

        Intent outIntent = getIntent();
        String index = outIntent.getStringExtra("ListID");
        clickedIndex = Integer.parseInt(index); //선택 일정 인덱스

        back_Btn = findViewById(R.id.backBtn);
        satrtLoc_Btn = findViewById(R.id.my_btn1);
        my_roc = findViewById(R.id.my_roc2);
        re_cafe_btn = findViewById(R.id.re_cafe_btn);
        re_res_btn = findViewById(R.id.re_res_btn);
        switchView = findViewById(R.id.switch1);
        re_map_text = findViewById(R.id.re_map_text);

        roc = my_roc.getText().toString();
        re_map_text.setText("'" + roc + "' 근처 추천 지도 보기");

        // 참여 유무 스위치 체인지
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //True이면 할 일
                    satrtLoc_Btn.setText("시작 위치 설정하기");
                    satrtLoc_Btn.setEnabled(true);
                }else{
                    //False이면 할 일
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