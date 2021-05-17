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
import android.widget.TextView;
import android.widget.Toast;

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
    //로그캣 사용 설정
    private static final String TAG = "MainActivity";

    //객체 선언
    SupportMapFragment mapFragment;
    private GoogleMap map;
    Button btnLocation, btnKor2Loc;
    EditText editText;
    TextView textView;

    MarkerOptions myMarker;

    //static final String TAG = "PlaceAutocomplete";
    AutocompleteSupportFragment autocompleteFragment;


    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_location);

        //권한 설정
        //checkDangerousPermissions();

        Places.initialize(getApplicationContext(), "AIzaSyCxKA49sPjrLo0hvNDkgcBt3VVwQuiQ94s");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        //객체 초기화
        editText = findViewById(R.id.editText);
        //btnLocation = findViewById(R.id.button1);
        btnKor2Loc = findViewById(R.id.button2); //확인_Btn
        textView = findViewById(R.id.textView);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Fragment 추가
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                textView.setText(place.getName() + " - " + place.getLatLng());
                Log.e("TAG", "Place: " + place.getName() + ", " + place.getLatLng());
            }
            @Override
            public void onError(Status status) {
                Log.i("TAG", "An error occurred: " + status);
            }
        });

        /*
        // Intent 추가
        Places.initialize(getApplicationContext(), "AIzaSyCxKA49sPjrLo0hvNDkgcBt3VVwQuiQ94s");
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //private static int AUTOCOMPLETE_REQUEST_CODE = 1;
                //List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ID, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                        .build(StartLocationActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
        */
        // 확인_Btn_Click
        btnKor2Loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().length() > 0) { //editText에 입력이 있다면
                    Location input_location = getLocationFromAddress(getApplicationContext(), editText.getText().toString());

                    //showCurrentLocation(location);
                    LatLng input_latLng = new LatLng(input_location.getLatitude(), input_location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLng(input_latLng));
                    map.moveCamera(CameraUpdateFactory.zoomTo(15));
                    myMarker = new MarkerOptions().position(input_latLng).title(editText.getText().toString());
                    map.addMarker(myMarker);

                    //위도, 경도 출력
                    textView.setText(String.format("위도: %f, 경도: %f",input_location.getLatitude(), input_location.getLongitude()));
                }
            }
        });
    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == AUTOCOMPLETE_REQUEST_CODE) && (resultCode == RESULT_OK)) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            editText.setText(place.getAddress());
            textView.setText(String.valueOf(place.getLatLng()));
            //Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            //Log.i(TAG, status.getStatusMessage());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        LatLng latLng = new LatLng(37.555172, 126.970800);
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
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
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

/*
        //지도 프래그먼트 설정
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "onMapReady: ");
                map = googleMap;
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                map.setMyLocationEnabled(true);
            }
        });
        MapsInitializer.initialize(this);

        //위치 확인 버튼 기능 추가
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMyLocation();
            }
        });
*/

/*
    private void requestMyLocation() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            long minTime = 1000;    //갱신 시간
            float minDistance = 0;  //갱신에 필요한 최소 거리

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    showCurrentLocation(location);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void showCurrentLocation(Location location) {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
        String msg = "Latitutde : " + curPoint.latitude
                + "\nLongitude : " + curPoint.longitude;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        //화면 확대, 숫자가 클수록 확대
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        //마커 찍기
        Location targetLocation = new Location("");
        targetLocation.setLatitude(37.4937); //위도
        targetLocation.setLongitude(127.0643); //경도
        showMyMarker(targetLocation);
    }

    //------------------권한 설정 시작------------------------
    private void checkDangerousPermissions() {
        String[] permissions = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    //------------------권한 설정 끝------------------------

    private void showMyMarker(Location location) {
        if(myMarker == null) {
            myMarker = new MarkerOptions();
            myMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
            myMarker.title("◎ 내위치\n");
            myMarker.snippet("여기가 어디지?");
            myMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.loc));
            map.addMarker(myMarker);
        }
    }
}*/