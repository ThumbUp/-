package com.example.thumbup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MeetingPlace extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final int voteCount[] = new int[9];
        for (int i = 0; i < 9; i++)
            voteCount[i] = 0;

        RadioButton radioButton[] = new RadioButton[3];
        Integer radioId[] = {R.id.place1, R.id.place2, R.id.place3};

        final String radioName[] = {"성신여대입구", "충정로", "혜화"};

        for (int i = 0; i < radioId.length; i++) {
            final int index; // 주의! 꼭 필요함..
            index = i;
            radioButton[index] = (RadioButton) findViewById(radioId[index]);
            radioButton[index].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // 투표수 증가.
                    voteCount[index]++;
                    Toast.makeText(getApplicationContext(),
                            radioName[index] + ": 총 " + voteCount[index] + " 표",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        TextView btnFinish = (TextView) findViewById(R.id.vote_place);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        MeetingActivity.class);
                intent.putExtra("VoteCount", voteCount);
                intent.putExtra("RadioName", radioName);
                startActivity(intent);
            }
        });
    }
}
