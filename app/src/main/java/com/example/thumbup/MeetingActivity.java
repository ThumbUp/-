package com.example.thumbup;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MeetingActivity extends AppCompatActivity {
    ListView meetingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting);

        meetingList = (ListView) findViewById(R.id.meeting_list);
        List<String> meetingData = new ArrayList<>();

//        ArrayAdapter<String> adapter = new ArrayAdapter();
    }
}
