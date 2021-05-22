package com.example.thumbup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

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

        Intent intent = getIntent();
        int[] voteResult = intent.getIntArrayExtra("VoteCount");
        String[] radioName = intent.getStringArrayExtra("RadioName");

        TextView final_roc = (TextView) findViewById(R.id.final_roc2);
        int maxEntry = 0;
        for (int i = 1; i < voteResult.length; i++) {
            if (voteResult[maxEntry] < voteResult[i])
                maxEntry = i;
        }
        final_roc.setText(radioName[maxEntry]);
    }
}
