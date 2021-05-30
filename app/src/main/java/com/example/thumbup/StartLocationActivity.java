package com.example.thumbup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thumbup.DataBase.DBManager;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StartLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    private GoogleMap map;

    ImageView back_btn;
    Button locOK_btn;

    String roc;
    int mykey;
    int scheduleindex;

    double Lati, Longi; //위도와 경도를 저장할 변수
    String my_place; //사용자가 설정한 위치를 저장할 변수

    DBManager dbManager = DBManager.getInstance();

    MarkerOptions myMarker;

    AutocompleteSupportFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_location);

        Places.initialize(getApplicationContext(), "AIzaSyCxKA49sPjrLo0hvNDkgcBt3VVwQuiQ94s");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        back_btn = findViewById(R.id.backBtn);
        locOK_btn = findViewById(R.id.OK_btn); //확인_Btn

        Intent outIntent2 = getIntent();
        roc = outIntent2.getStringExtra("Rocation");
        mykey = outIntent2.getIntExtra("MyKey", 0);
        scheduleindex = outIntent2.getIntExtra("ScheduleIndex", 0);

        Log.e("MY KEY : ", mykey+"");
        Log.e("MY S_INDEX : ", scheduleindex+"");

        Location location = getLocationFromAddress(getApplicationContext(), roc);

        Lati = location.getLatitude();
        Longi = location.getLongitude();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent outIntent = new Intent(getApplicationContext(), SubSchedule.class);

        // 자동완성 Fragment
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.e("TAG", "Place: " + place.getName() + ", " + place.getLatLng());

                Location input_location = getLocationFromAddress(getApplicationContext(), place.getName());

                my_place = place.getName(); //설정 위치 저장
                Lati = input_location.getLatitude(); //해당 위치의 위도와
                Longi = input_location.getLongitude(); //경도 저장
                LatLng input_latLng = new LatLng(Lati, Longi);

                map.moveCamera(CameraUpdateFactory.newLatLng(input_latLng));
                map.moveCamera(CameraUpdateFactory.zoomTo(15));
                myMarker = new MarkerOptions().position(input_latLng).title(place.getName());
                map.addMarker(myMarker);
            }
            @Override
            public void onError(Status status) {
                Log.i("TAG", "An error occurred: " + status);
            }
        });

        // 뒤로가기 버튼 클릭
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 확인 버튼 클릭
        locOK_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outIntent.putExtra("Place", my_place); //설정 위치 전달

                // 일정 안에 나의 위도/경도 데이터 변경
                dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(scheduleindex).members.get(mykey).latitude = Lati;
                dbManager.participatedMeetings.get("-MaZIcU6ZjxsYF_iX-6k").schedules.get(scheduleindex).members.get(mykey).longitude = Longi;
                dbManager.UpdateMeeting("-MaZIcU6ZjxsYF_iX-6k");

                setResult(RESULT_OK, outIntent);
                finish();

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        LatLng latLng = new LatLng(Lati, Longi);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.moveCamera(CameraUpdateFactory.zoomTo(15));
        myMarker = new MarkerOptions().position(latLng).title("서울역");
        map.addMarker(myMarker);

        //GPS 사용 권한 퍼미션 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            checkLocationPermissionWithRationale();
        }
    }

    //GPS 사용 허가 알림
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermissionWithRationale() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("위치정보")
                        .setMessage("이 앱을 사용하기 위해서는 위치정보에 접근이 필요합니다. 위치정보 접근을 허용하여 주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(StartLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        map.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    // 주소 -> 위도, 경도 변환
    private Location getLocationFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;
        Location resLocation = new Location("");
        try {
            addresses = geocoder.getFromLocationName(address, 10);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (addresses != null) {
            if (addresses.size() == 0) {
                Toast.makeText(getApplicationContext(), "해당되는 주소 정보를 찾지 못했습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Address addressLoc = addresses.get(0);
                resLocation.setLatitude(addressLoc.getLatitude()); //위도
                resLocation.setLongitude(addressLoc.getLongitude()); //경도
            }
        }
        return resLocation;
    }
}

