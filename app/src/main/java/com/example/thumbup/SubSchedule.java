package com.example.thumbup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SubSchedule extends AppCompatActivity {

    Button satrtLoc_Btn;
    TextView my_roc;
    Button re_cafe_btn;
    //TextView re_place;

    String roc; //설정 위치
    double roc_lati, roc_longi; //해당 위치의 위도와 경도 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_schedule);

        satrtLoc_Btn = findViewById(R.id.my_btn1);
        my_roc = findViewById(R.id.my_roc2);
        re_cafe_btn = findViewById(R.id.re_cafe_btn);
        //re_place = findViewById(R.id.re_place);

        roc = my_roc.getText().toString();
        re_cafe_btn.setText("'" + roc + "' 주변 카페 지도 보기");

        satrtLoc_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StartLocationActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        re_cafe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecommendPlaceActivity.class);
                intent.putExtra("Rocation", roc);
                startActivityForResult(intent, 1);
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
            re_cafe_btn.setText("'" + roc + "' 주변 카페 지도 보기");
        }
    }

}